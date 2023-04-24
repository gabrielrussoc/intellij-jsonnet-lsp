package com.databricks.intellijjsonnet

import com.intellij.lexer.FlexAdapter

class JsonnetLexerAdapter : FlexAdapter(com.databricks.intellijjsonnet.JsonnetLexer(null))