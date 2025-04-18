// This is a generated file. Not intended for manual editing.
package com.explyt.jpa.ql.psi.impl;

import java.util.List;

import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;

import static com.explyt.jpa.ql.psi.JpqlTypes.*;

import com.explyt.jpa.ql.psi.*;

public class JpqlAliasDeclarationImpl extends JpqlNameIdentifierOwnerImpl implements JpqlAliasDeclaration {

  public JpqlAliasDeclarationImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull JpqlVisitor visitor) {
    visitor.visitAliasDeclaration(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof JpqlVisitor) accept((JpqlVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public JpqlIdentifier getIdentifier() {
    return findNotNullChildByClass(JpqlIdentifier.class);
  }

  @Override
  @NotNull
  public PsiElement setName(@NotNull String newName) {
    return JpqlPsiImplUtil.setName(this, newName);
  }

  @Override
  @NotNull
  public String getName() {
    return JpqlPsiImplUtil.getName(this);
  }

  @Override
  @Nullable
  public PsiElement getReferencedElement() {
    return JpqlPsiImplUtil.getReferencedElement(this);
  }

  @Override
  @Nullable
  public PsiElement getNameIdentifier() {
    return JpqlPsiImplUtil.getNameIdentifier(this);
  }

}
