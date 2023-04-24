package com.databricks.intellijjsonnet

import com.databricks.intellijjsonnet.psi.JsonnetTypes
import com.intellij.lexer.Lexer
import com.intellij.openapi.editor.DefaultLanguageHighlighterColors
import com.intellij.openapi.editor.HighlighterColors
import com.intellij.openapi.editor.colors.TextAttributesKey
import com.intellij.openapi.fileTypes.SyntaxHighlighterBase
import com.intellij.psi.TokenType
import com.intellij.psi.tree.IElementType

class JsonnetSyntaxHighlighter : SyntaxHighlighterBase() {
    override fun getHighlightingLexer(): Lexer {
        return JsonnetLexerAdapter()
    }

    override fun getTokenHighlights(tokenType: IElementType): Array<TextAttributesKey> {
        return when (tokenType) {
            com.databricks.intellijjsonnet.psi.JsonnetTypes.BLOCK_COMMENT -> {
                COMMENT_KEYS
            }
            com.databricks.intellijjsonnet.psi.JsonnetTypes.LINE_COMMENT -> {
                COMMENT_KEYS
            }
            com.databricks.intellijjsonnet.psi.JsonnetTypes.TRUE, com.databricks.intellijjsonnet.psi.JsonnetTypes.FALSE, com.databricks.intellijjsonnet.psi.JsonnetTypes.NULL, com.databricks.intellijjsonnet.psi.JsonnetTypes.IMPORT, com.databricks.intellijjsonnet.psi.JsonnetTypes.IMPORTSTR, com.databricks.intellijjsonnet.psi.JsonnetTypes.LOCAL, com.databricks.intellijjsonnet.psi.JsonnetTypes.FUNCTION, com.databricks.intellijjsonnet.psi.JsonnetTypes.IN, com.databricks.intellijjsonnet.psi.JsonnetTypes.IF, com.databricks.intellijjsonnet.psi.JsonnetTypes.THEN, com.databricks.intellijjsonnet.psi.JsonnetTypes.ELSE, com.databricks.intellijjsonnet.psi.JsonnetTypes.SUPER, com.databricks.intellijjsonnet.psi.JsonnetTypes.ERROR, com.databricks.intellijjsonnet.psi.JsonnetTypes.SELF, com.databricks.intellijjsonnet.psi.JsonnetTypes.FOR, com.databricks.intellijjsonnet.psi.JsonnetTypes.ASSERT, com.databricks.intellijjsonnet.psi.JsonnetTypes.DOLLAR -> {
                KEYWORD_KEYS
            }
            com.databricks.intellijjsonnet.psi.JsonnetTypes.NUMBER -> {
                NUMBER_KEYS
            }
            com.databricks.intellijjsonnet.psi.JsonnetTypes.SINGLE_QUOTED_STRING, com.databricks.intellijjsonnet.psi.JsonnetTypes.DOUBLE_QUOTED_STRING, com.databricks.intellijjsonnet.psi.JsonnetTypes.VERBATIM_DOUBLE_QUOTED_STRING, com.databricks.intellijjsonnet.psi.JsonnetTypes.VERBATIM_SINGLE_QUOTED_STRING, com.databricks.intellijjsonnet.psi.JsonnetTypes.TRIPLE_BAR_QUOTED_STRING -> {
                STRING_KEYS
            }
            TokenType.BAD_CHARACTER -> {
                BAD_CHAR_KEYS
            }
            else -> {
                EMPTY_KEYS
            }
        }
    }

    companion object {
        val BAD_CHARACTER =
            TextAttributesKey.createTextAttributesKey("JSONNET_BAD_CHARACTER", HighlighterColors.BAD_CHARACTER)
        val KEY = TextAttributesKey.createTextAttributesKey("JSONNET_KEY", DefaultLanguageHighlighterColors.KEYWORD)
        val VALUE = TextAttributesKey.createTextAttributesKey("JSONNET_VALUE", DefaultLanguageHighlighterColors.STRING)
        val COMMENT =
            TextAttributesKey.createTextAttributesKey("JSONNET_COMMENT", DefaultLanguageHighlighterColors.LINE_COMMENT)
        val NUMBER =
            TextAttributesKey.createTextAttributesKey("JSONNET_NUMBER", DefaultLanguageHighlighterColors.NUMBER)
        private val BAD_CHAR_KEYS = arrayOf(BAD_CHARACTER)
        private val KEYWORD_KEYS = arrayOf(KEY)
        private val STRING_KEYS = arrayOf(VALUE)
        private val COMMENT_KEYS = arrayOf(COMMENT)
        private val EMPTY_KEYS = arrayOf<TextAttributesKey>()
        private val NUMBER_KEYS = arrayOf(NUMBER)
    }
}