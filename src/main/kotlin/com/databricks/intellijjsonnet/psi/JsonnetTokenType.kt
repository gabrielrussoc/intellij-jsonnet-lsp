// Copyright 2000-2022 JetBrains s.r.o. and other contributors. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.databricks.intellijjsonnet.psi

import com.databricks.intellijjsonnet.JsonnetLanguage
import com.intellij.psi.tree.IElementType

class JsonnetTokenType(debugName: String) :
    IElementType(debugName, JsonnetLanguage.INSTANCE) {
    override fun toString(): String {
        return "JsonnetTokenType." + super.toString()
    }
}