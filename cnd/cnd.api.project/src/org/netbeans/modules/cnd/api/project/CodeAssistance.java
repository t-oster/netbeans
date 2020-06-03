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

package org.netbeans.modules.cnd.api.project;

import java.util.List;
import javax.swing.event.ChangeListener;
import org.openide.filesystems.FileObject;
import org.openide.util.Pair;

/**
 *
 */
public interface CodeAssistance {
    enum State {
        ParsedSource, ParsedOrphanHeader, ParsedIncludedHeader, NotParsed
    }
    boolean hasCodeAssistance(NativeFileItem item);    
    State getCodeAssistanceState(NativeFileItem item);
    Pair<NativeFileItem.Language, NativeFileItem.LanguageFlavor> getStartFileLanguageFlavour(NativeFileItem item);
    Pair<NativeFileItem.Language, NativeFileItem.LanguageFlavor> getHeaderLanguageFlavour(FileObject item);
    void addChangeListener(ChangeListener listener);
    void removeChangeListener(ChangeListener listener);
    List<NativeFileItem> findHeaderCompilationUnit(NativeFileItem item);
}
