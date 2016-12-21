package com.wikia.webdriver.elements.mercury.components.discussions.mobile;

import com.wikia.webdriver.pageobjectsfactory.pageobject.BasePageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;


public class ReplyCreatorMobile extends BasePageObject {

  @FindBy(css = ".discussion-editor-entry-point-container .discussion-editor-entry-point-content")
  private WebElement replyCreator;

  @FindBy(css = ".modal-dialog-posting-not-allowed.is-visible .modal-dialog")
  private WebElement dialogSignIn;

  @FindBy(css = ".modal-dialog-posting-not-allowed.is-visible .confirm-button")
  private WebElement okButtonInSignInDialog;

  @FindBy(css = ".modal-dialog-posting-not-allowed.is-visible .signin-button")
  private WebElement signInButtonInSignInDialog;

  @FindBy(css = ".editor-overlay-message .message-button")
  private WebElement guidelinesReadButton;

  @FindBy(css = ".discussion-standalone-editor .discussion-standalone-editor-save-button")
  private WebElement submitButton;

  @FindBy(css = ".discussion-standalone-editor .discussion-standalone-editor-textarea:not([disabled])")
  private WebElement textarea;

  public boolean isPresent() {
    return !driver.findElements(By.className("discussion-editor-entry-point-container")).isEmpty();
  }

  public ReplyCreatorMobile click() {
    replyCreator.click();
    return this;
  }

  public boolean isModalDialogVisible() {
    return dialogSignIn.isDisplayed();
  }

  public ReplyCreatorMobile clickOkButtonInSignInDialog() {
    okButtonInSignInDialog.click();
    return this;
  }

  public ReplyCreatorMobile clickSignInButtonInSignInDialog() {
    signInButtonInSignInDialog.click();
    return this;
  }

  public ReplyCreatorMobile clickGuidelinesReadButton() {
    guidelinesReadButton.click();
    return this;
  }

  public ReplyCreatorMobile add(final String text) {
    textarea.sendKeys(text);
    return this;
  }

  public ReplyCreatorMobile clickSubmitButton() {
    submitButton.click();
    return this;
  }
}
