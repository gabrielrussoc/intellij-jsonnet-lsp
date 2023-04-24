package com.databricks.intellijjsonnet

import com.databricks.intellijjsonnet.psi.JsonnetTypes
import com.intellij.lang.BracePair
import com.intellij.lang.PairedBraceMatcher
import com.intellij.psi.PsiFile
import com.intellij.psi.tree.IElementType

class JsonnetBraceMatcher : PairedBraceMatcher {
    override fun getPairs(): Array<BracePair> {
        return PAIRS
    }

    override fun isPairedBracesAllowedBeforeType(type: IElementType, tokenType: IElementType?): Boolean {
        return false
    }

    override fun getCodeConstructStart(file: PsiFile, openingBraceOffset: Int): Int {
        return openingBraceOffset
    }

    companion object {
        private val PAIRS = arrayOf(
            BracePair(com.databricks.intellijjsonnet.psi.JsonnetTypes.L_BRACKET, com.databricks.intellijjsonnet.psi.JsonnetTypes.R_BRACKET, false),
            BracePair(com.databricks.intellijjsonnet.psi.JsonnetTypes.L_PAREN, com.databricks.intellijjsonnet.psi.JsonnetTypes.R_PAREN, false),
            BracePair(com.databricks.intellijjsonnet.psi.JsonnetTypes.L_CURLY, com.databricks.intellijjsonnet.psi.JsonnetTypes.R_CURLY, true)
        )
    }
}