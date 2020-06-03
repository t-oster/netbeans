/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */

package org.netbeans.modules.subversion.remote.ui.history;

import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.util.List;
import java.util.logging.Level;
import javax.swing.SwingUtilities;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;
import javax.swing.text.EditorKit;
import javax.swing.text.StyleContext;
import javax.swing.text.StyledDocument;
import javax.swing.text.StyledEditorKit;
import org.netbeans.api.diff.DiffController;
import org.netbeans.api.diff.Difference;
import org.netbeans.api.diff.StreamSource;
import org.netbeans.modules.subversion.remote.Subversion;
import org.netbeans.modules.subversion.remote.api.SVNUrl;
import org.netbeans.modules.subversion.remote.client.SvnProgressSupport;
import org.netbeans.modules.subversion.remote.util.SvnUtils;
import org.netbeans.modules.versioning.core.api.VCSFileProxy;
import org.openide.cookies.EditorCookie;
import org.openide.filesystems.FileObject;
import org.openide.filesystems.FileSystem;
import org.openide.filesystems.FileUtil;
import org.openide.loaders.DataObject;
import org.openide.text.CloneableEditorSupport;
import org.openide.util.Cancellable;
import org.openide.util.Lookup;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;

/**
 * DiffResultsView not showing differences but rather fixed line numbers.
 * Currently used by bugtracking to display revisions of a file and to fix the view on a given line number.
 * 
 * 
 */
final class DiffResultsViewForLine extends DiffResultsView {
    private final int lineNumber;
    
    public DiffResultsViewForLine(final SearchHistoryPanel parent, final List<RepositoryRevision> results, final int lineNumber) {
        super(parent, results);
        this.lineNumber = Math.max(lineNumber - 1, 0);
        setButtonLabels();
    }

    @Override
    protected void showRevisionDiff(RepositoryRevision.Event rev, boolean showLastDifference) {
        if (rev.getFile() == null) {
            return;
        }
        showDiff(rev.getLogInfoHeader().getRepositoryRootUrl(), null, rev, showLastDifference);
    }

    @Override
    protected SvnProgressSupport createShowDiffTask (RepositoryRevision.Event revision1, RepositoryRevision.Event revision2, boolean showLastDifference) {
        if (revision1 == null) {
            return new ShowDiffTask(parent.getFileSystem(), revision2, revision2.getLogInfoHeader().getLog().getRevision().toString(), showLastDifference);
        } else {
            return super.createShowDiffTask(revision1, revision2, showLastDifference);
        }
    }

    @Override
    void onNextButton() {
        if (++currentIndex >= treeView.getRowCount()) {
            currentIndex = 0;
        }
        setDiffIndex(currentIndex, false);
    }

    @Override
    void onPrevButton() {
        if (--currentIndex < 0) {
            currentIndex = treeView.getRowCount() - 1;
        }
        setDiffIndex(currentIndex, true);
    }

    private void setButtonLabels() {
        parent.bNext.getAccessibleContext().setAccessibleName(NbBundle.getMessage(DiffResultsViewForLine.class, "ACSN_NextRevision")); // NOI18N
        parent.bNext.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(DiffResultsViewForLine.class, "ACSD_NextRevision")); // NOI18N
        parent.bNext.setToolTipText(NbBundle.getMessage(DiffResultsViewForLine.class, "ACSD_NextRevision")); // NOI18N
        parent.bPrev.getAccessibleContext().setAccessibleName(NbBundle.getMessage(DiffResultsViewForLine.class, "ACSN_PrevRevision")); // NOI18N
        parent.bPrev.getAccessibleContext().setAccessibleDescription(NbBundle.getMessage(DiffResultsViewForLine.class, "ACSD_PrevRevision")); // NOI18N
        parent.bPrev.setToolTipText(NbBundle.getMessage(DiffResultsViewForLine.class, "ACSD_PrevRevision")); // NOI18N
    }

    private class ShowDiffTask extends SvnProgressSupport {
        private final RepositoryRevision.Event header;
        private final String revision2;

        private ShowDiffTask(FileSystem fileSystem, RepositoryRevision.Event header, String revision2, boolean showLastDifference) {
            super(fileSystem);
            this.header = header;
            this.revision2 = revision2;
        }

        @Override
        protected void perform() {
            showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_LoadingDiff")); // NOI18N
            SVNUrl repotUrl = header.getLogInfoHeader().getRepositoryRootUrl();
            SVNUrl fileUrl = repotUrl.appendPath(header.getChangedPath().getPath());
            // through peg revision always except from 'deleting the file', since the file does not exist in the newver revision
            final DiffStreamSource leftSource = new DiffStreamSource(getFileSystem(), header.getFile(), repotUrl, fileUrl, revision2, revision2);
            final LocalFileDiffStreamSource rightSource = new LocalFileDiffStreamSource(header.getFile(), true);
            this.setCancellableDelegate(new Cancellable() {
                @Override
                public boolean cancel() {
                    leftSource.cancel();
                    return true;
                }
            });
            // it's enqueued at ClientRuntime queue and does not return until previous request handled
            leftSource.getMIMEType();  // triggers s1.init()
            if (isCanceled()) {
                showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_NoRevisions")); // NOI18N
                return;
            }
            rightSource.getMIMEType();  // triggers s2.init()
            if (isCanceled()) {
                showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_NoRevisions")); // NOI18N
                return;
            }

            if (currentTask != this) {
                return;
            }

            SwingUtilities.invokeLater(new Runnable() {
                @Override
                public void run() {
                    try {
                        if (isCanceled()) {
                            showDiffError(NbBundle.getMessage(DiffResultsView.class, "MSG_DiffPanel_NoRevisions")); // NOI18N
                            return;
                        }
                        final DiffController view = DiffController.createEnhanced(leftSource, rightSource);
                        int leftMaxLineNumber = getLastLineIndex(leftSource);
                        int rightMaxLineNumber = getLastLineIndex(rightSource);
                        if (currentTask == ShowDiffTask.this) {
                            currentDiff = view;
                            setBottomComponent(currentDiff.getJComponent());
                            if (leftMaxLineNumber != -1) {
                                setLocation(Math.min(leftMaxLineNumber, lineNumber), false);
                            }
                            if (rightMaxLineNumber != -1) {
                                setLocation(Math.min(rightMaxLineNumber, lineNumber), true);
                            }
                            parent.refreshComponents(false);
                        }
                    } catch (IOException e) {
                        Subversion.LOG.log(Level.INFO, null, e);
                    }
                }
            });
        }
    }

    private int getLastLineIndex (final StreamSource ss) {
        String mimeType = ss.getMIMEType();
        if (mimeType == null || !mimeType.startsWith("text/")) { //NOI18N
            Subversion.LOG.log(Level.INFO, "Wrong mime type");
            return 0;
        }
        EditorKit kit = CloneableEditorSupport.getEditorKit(mimeType);
        if (kit == null) {
            Subversion.LOG.log(Level.WARNING, "No editor kit available");
            return 0;
        }
        Document sdoc = getSourceDocument(ss);
        Document doc = sdoc != null ? sdoc : kit.createDefaultDocument();
        StyledDocument styledDoc;
        if ((doc instanceof StyledDocument)) {
            styledDoc = (StyledDocument) doc;
        } else {
            styledDoc = new DefaultStyledDocument(new StyleContext());
            kit = new StyledEditorKit();
        }
        if (sdoc == null) {
            Reader r = null;
            try {
                r = ss.createReader();
                if (r != null) {
                    try {
                        kit.read(r, styledDoc, 0);
                    } catch (javax.swing.text.BadLocationException e) {
                        throw new IOException("Can not locate the beginning of the document."); // NOI18N
                    }
                }
            } catch (IOException ex) {
                Subversion.LOG.log(Level.INFO, null, ex);
            } finally {
                try {
                    if (r != null) {
                        r.close();
                    }
                } catch (IOException ex) {
                    Subversion.LOG.log(Level.INFO, null, ex);
                }
            }
        }
        return org.openide.text.NbDocument.findLineNumber(styledDoc, styledDoc.getEndPosition().getOffset());
    }

    private Document getSourceDocument(StreamSource ss) {
        Document sdoc = null;
        FileObject fo = ss.getLookup().lookup(FileObject.class);
        if (fo != null) {
            try {
                DataObject dao = DataObject.find(fo);
                if (dao.getPrimaryFile() == fo) {
                    EditorCookie ec = dao.getLookup().lookup(EditorCookie.class);
                    if (ec != null) {
                        sdoc = ec.openDocument();
                    }
                }
            } catch (Exception e) {
                // fallback to other means of obtaining the source
            }
        } else {
            sdoc = ss.getLookup().lookup(Document.class);
        }
        return sdoc;
    }

    private void setLocation (final int lineNumber, final boolean showLineInLocal) {
        if (currentDiff == null) {
            return;
        }
        if (showLineInLocal) {
            currentDiff.setLocation(DiffController.DiffPane.Modified, DiffController.LocationType.LineNumber, lineNumber);
        } else {
            currentDiff.getJComponent().putClientProperty("diff.smartScrollDisabled", Boolean.TRUE); //NOI18N
            currentDiff.setLocation(DiffController.DiffPane.Base, DiffController.LocationType.LineNumber, lineNumber);
        }
    }

    private static class LocalFileDiffStreamSource extends StreamSource {

        private final FileObject    fileObject;
        private final boolean       isRight;
        private final VCSFileProxy file;
        private String mimeType;

        public LocalFileDiffStreamSource (VCSFileProxy file, boolean isRight) {
            this.file = file.normalizeFile();
            this.fileObject = file.toFileObject();
            this.isRight = isRight;
        }

        @Override
        public boolean isEditable() {
            return isRight && fileObject != null && fileObject.canWrite();
        }

        @Override
        public Lookup getLookup() {
            if (fileObject != null) {
                return Lookups.fixed(fileObject);
            } else {
                return Lookups.fixed();
            }
        }

        @Override
        public String getName() {
            return file.getName();
        }

        @Override
        public String getTitle() {
            return fileObject != null ? FileUtil.getFileDisplayName(fileObject) : file.getPath();
        }

        @Override
        public String getMIMEType() {
            return mimeType = fileObject != null && fileObject.isValid() ? SvnUtils.getMimeType(file) : null;
        }

        @Override
        public Reader createReader() throws IOException {
            if (mimeType == null || !mimeType.startsWith("text/")) { //NOI18N
                return null;
            } else {
                return org.netbeans.modules.versioning.util.Utils.createReader(file.toFileObject());
            }
        }

        @Override
        public Writer createWriter(Difference[] conflicts) throws IOException {
            return null;
        }
    }
}
