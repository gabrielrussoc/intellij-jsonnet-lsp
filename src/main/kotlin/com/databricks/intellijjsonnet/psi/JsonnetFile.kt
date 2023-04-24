package com.databricks.intellijjsonnet.psi

import com.databricks.intellijjsonnet.JsonnetFileType
import com.databricks.intellijjsonnet.JsonnetLanguage
import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class JsonnetFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, JsonnetLanguage.INSTANCE) {
    override fun getFileType(): FileType {
        return JsonnetFileType.INSTANCE
    }

    override fun toString(): String {
        return "Jsonnet File"
    }
}