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

#include "file.h"
 
void ClassA::aPubFun() {
    ClassA a;
    ClassB b;
    ClassC c;
    ClassD d;
    ClassE e;
     //  <- test text is inserted here
}

void ClassB::bProtFun() {
    ClassA a;
    ClassB b;
    ClassC c;
    ClassD d;
    ClassE e;   
     //  <- test text is inserted here
}

void ClassC::cPrivFun() {
    ClassA a;
    ClassB b;
    ClassC c;
    ClassD d;
    ClassE e;
     //  <- test text is inserted here
}

void ClassD::dPubFun() {
    ::ClassA a; 
    ClassB b;
    ClassC c;
    ClassD d;
    ClassE e;
     //  <- test text is inserted here
}

void ClassE::ePubFun() {
    ClassA a;
    ClassB b;
    ClassC c;
    ClassD d;
    ClassE e;
     //  <- test text is inserted here
}

void friendOfB() {
    ClassA a;
    ClassB b;
    ClassC c;
    ClassD d;
    ClassE e;
     //  <- test text is inserted here
}

