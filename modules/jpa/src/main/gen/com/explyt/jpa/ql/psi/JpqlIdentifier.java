// This is a generated file. Not intended for manual editing.
package com.explyt.jpa.ql.psi;

import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiPolyVariantReference;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface JpqlIdentifier extends JpqlNamedElement {

  @Nullable
  PsiElement getId();

  @NotNull
  PsiElement setName(@NotNull String newName);

  @NotNull
  String getName();

  @Nullable
  PsiPolyVariantReference getReference();

}
