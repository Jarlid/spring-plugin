// This is a generated file. Not intended for manual editing.
package com.explyt.spring.web.language.http.psi;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.psi.PsiElement;

public interface HttpComment extends PsiElement {

  @NotNull
  List<HttpVariable> getVariableList();

  @Nullable
  PsiElement getCommentLine();

  @Nullable
  PsiElement getCommentSeparator();

  @Nullable
  PsiElement getTagToken();

}
