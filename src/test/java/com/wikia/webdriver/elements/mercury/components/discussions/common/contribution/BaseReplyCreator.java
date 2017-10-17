package com.wikia.webdriver.elements.mercury.components.discussions.common.contribution;

import com.wikia.webdriver.common.core.helpers.ContentLoader;
import com.wikia.webdriver.elements.mercury.components.discussions.common.TextGenerator;
import com.wikia.webdriver.pageobjectsfactory.pageobject.BasePageObject;
import lombok.Getter;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.net.URL;

public abstract class BaseReplyCreator extends BasePageObject implements Editor {

  @Getter
  private By errorNotification = By.className("error");

  protected abstract WebElement getBaseReplyCreatorTextArea();
  protected abstract WebElement getEditor();
  protected abstract WebElement getDialogSignIn();
  protected abstract WebElement getOkButtonInSignInDialog();
  protected abstract WebElement getSignInButtonInSignInDialog();
  protected abstract WebElement getGuidelinesReadButton();
  protected abstract WebElement getTextarea();
  protected abstract WebElement getSubmitButton();
  protected abstract WebElement getLoadingSuccess();
  protected abstract WebElement getImagePreview();
  protected abstract WebElement getUploadButton();
  protected abstract WebElement getImageDeleteButton();
  protected abstract By getOpenGraphContainer();
  protected abstract By getOpenGraphText();

  @Override
  public BaseReplyCreator click() {
    wait.forElementVisible(getBaseReplyCreatorTextArea()).click();
    return this;
  }

  @Override
  public boolean isModalDialogVisible() {
    return wait.forElementVisible(getDialogSignIn()).isDisplayed();
  }

  @Override
  public BaseReplyCreator clickOkButtonInSignInDialog() {
    getOkButtonInSignInDialog().click();
    return this;
  }


  @Override
  public BaseReplyCreator clickSignInButtonInSignInDialog() {
    getSignInButtonInSignInDialog().click();
    return this;
  }

  @Override
  public BaseReplyCreator clickGuidelinesReadButton() {
    getGuidelinesReadButton().click();
    return this;
  }

  @Override
  public BaseReplyCreator clearText() {
    wait.forElementVisible(getTextarea()).clear();
    return this;
  }

  @Override
  public BaseReplyCreator addTextWith(final String text) {
    wait.forElementVisible(getTextarea()).sendKeys(text);
    return this;
  }

  @Override
  public boolean isSubmitButtonActive() {
    return getSubmitButton().isEnabled();
  }

  @Override
  public BaseReplyCreator clickSubmitButton() {
    getSubmitButton().click();
    return this.waitForConfirmation();
  }

  private BaseReplyCreator waitForConfirmation() {
    waitSafely(() -> wait.forElementVisible(getLoadingSuccess()));
    waitSafely(() -> wait.forElementNotVisible(getLoadingSuccess()));
    return this;
  }

  public BaseReplyCreator uploadImage() {
    getUploadButton().sendKeys(ContentLoader.getImage());
    wait.forElementVisible(getImagePreview());
    return this;
  }

  public String uploadUnsupportedImage() {
    getUploadButton().sendKeys(ContentLoader.getUnsupportedImage());
    wait.forElementVisible(getErrorNotification()).getText();
    return wait.forElementVisible(getErrorNotification()).getText();
  }

  public BaseReplyCreator removeImage() {
    waitAndClick(getImageDeleteButton());
    wait.forElementNotVisible(getImagePreview());
    return this;
  }

  public boolean hasOpenGraph() {
    boolean result = false;
    final WebElement openGraphContainer = getEditor()
      .findElement(getOpenGraphContainer());
    if (null != openGraphContainer) {
      result = null != openGraphContainer.findElement(getOpenGraphText());
    }
    return result;
  }

  public BaseReplyCreator startReplyCreation() {
    return startReplyCreationWith(TextGenerator.defaultText());
  }

  @Override
  public BaseReplyCreator startReplyCreationWith(String description) {
    click().clickGuidelinesReadButton().addTextWith(description);
    return this;
  }

  public BaseReplyCreator startReplyCreationWithLink(URL link) {
    return startReplyCreationWith(String.format(" %s ", link.toString()));
  }

}
