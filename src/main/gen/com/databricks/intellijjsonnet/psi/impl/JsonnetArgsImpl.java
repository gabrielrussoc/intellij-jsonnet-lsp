// This is a generated file. Not intended for manual editing.
package com.databricks.intellijjsonnet.psi.impl;

import java.util.List;

import com.databricks.intellijjsonnet.psi.JsonnetArg;
import com.databricks.intellijjsonnet.psi.JsonnetArgs;
import com.databricks.intellijjsonnet.psi.JsonnetVisitor;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.databricks.intellijjsonnet.psi.JsonnetTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.databricks.intellijjsonnet.psi.*;

public class JsonnetArgsImpl extends ASTWrapperPsiElement implements JsonnetArgs {

  public JsonnetArgsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JsonnetVisitor visitor) {
    visitor.visitArgs(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JsonnetVisitor) accept((JsonnetVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<JsonnetArg> getArgList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JsonnetArg.class);
  }

}
