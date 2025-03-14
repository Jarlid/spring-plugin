// This is a generated file. Not intended for manual editing.
package com.explyt.jpa.ql.psi.impl;

import com.explyt.jpa.ql.psi.JpqlIdentifier;
import com.explyt.jpa.ql.psi.JpqlPathReferenceExpression;
import com.explyt.jpa.ql.psi.JpqlVisitor;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public class JpqlPathReferenceExpressionImpl extends JpqlReferenceExpressionImpl implements JpqlPathReferenceExpression {

  public JpqlPathReferenceExpressionImpl(@NotNull ASTNode node) {
    super(node);
  }

  @Override
  public void accept(@NotNull JpqlVisitor visitor) {
    visitor.visitPathReferenceExpression(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JpqlVisitor) accept((JpqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<JpqlIdentifier> getIdentifierList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, JpqlIdentifier.class);
  }

}
