// This is a generated file. Not intended for manual editing.
package generated.psi.impl;

import java.util.List;
import org.jetbrains.annotations.*;
import com.intellij.lang.ASTNode;
import com.intellij.psi.PsiElement;
import com.intellij.psi.PsiElementVisitor;
import com.intellij.psi.util.PsiTreeUtil;
import static com.explyt.spring.web.language.http.psi.HttpTypes.*;
import com.intellij.extapi.psi.ASTWrapperPsiElement;
import com.explyt.spring.web.language.http.psi.*;

public class HttpRequestsImpl extends ASTWrapperPsiElement implements HttpRequests {

  public HttpRequestsImpl(@NotNull ASTNode node) {
    super(node);
  }

  public void accept(@NotNull HttpVisitor visitor) {
    visitor.visitRequests(this);
  }

  @Override
  public void accept(@NotNull PsiElementVisitor visitor) {
    if (visitor instanceof HttpVisitor) accept((HttpVisitor)visitor);
    else super.accept(visitor);
  }

  @Override
  @NotNull
  public List<HttpAnyRequestBlock> getAnyRequestBlockList() {
    return PsiTreeUtil.getChildrenOfTypeAsList(this, HttpAnyRequestBlock.class);
  }

}
