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

package org.netbeans.cnd.spi.lexer;

import org.netbeans.api.lexer.InputAttributes;
import org.netbeans.api.lexer.Language;
import org.netbeans.api.lexer.LanguagePath;
import org.netbeans.api.lexer.Token;
import org.netbeans.cnd.api.lexer.CppTokenId;
import org.netbeans.spi.lexer.LanguageEmbedding;

/**
 * Provider for language embedding for CndLexer.
 *
 */
public interface CndLexerLanguageEmbeddingProvider {
    // constant to be used for registration of provider
    // i.e. @ServiceProvider(path=CndLexerLanguageEmbeddingProvider.REGISTRATION_PATH, service=CndLexerLanguageEmbeddingProvider.class, position=100)
    public static final String REGISTRATION_PATH = "CND/CndLexerLanguageEmbeddingProvider"; // NOI18N

    /**
     * Returns embedding for token if possible, null otherwise.
     *
     * @return language embedding or null if token is not recognized by provider
     */
    public LanguageEmbedding<?> createEmbedding(Token<CppTokenId> token, LanguagePath languagePath, InputAttributes inputAttributes);

    /**
     * check if language is known by provider
     * @param lang
     * @return 
     */
    public boolean isKnownLanguage(Language<?> lang);
}
