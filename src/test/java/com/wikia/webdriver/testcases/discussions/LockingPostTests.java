package com.wikia.webdriver.testcases.discussions;

import com.wikia.webdriver.common.contentpatterns.MercuryWikis;
import com.wikia.webdriver.common.core.Assertion;
import com.wikia.webdriver.common.core.annotations.Execute;
import com.wikia.webdriver.common.core.annotations.InBrowser;
import com.wikia.webdriver.common.core.drivers.Browser;
import com.wikia.webdriver.common.core.helpers.Emulator;
import com.wikia.webdriver.common.core.helpers.User;
import com.wikia.webdriver.common.remote.operations.DiscussionsOperations;
import com.wikia.webdriver.common.templates.NewTestTemplate;
import com.wikia.webdriver.elements.mercury.components.discussions.common.PostEntity;
import com.wikia.webdriver.elements.mercury.components.discussions.common.TextGenerator;
import com.wikia.webdriver.elements.mercury.pages.discussions.PostDetailsPage;
import org.testng.annotations.Test;

@Execute(onWikia = MercuryWikis.DISCUSSIONS_AUTO)
@Test(groups = "discussions-locking-posts")
public class LockingPostTests extends NewTestTemplate {

  public static final String SHOULD_LOCK_MESSAGE = "%s  should be able to lock the post.";

  public static final String SHOULD_UNLOCK_MESSAGE = "%s should be able to unlock post locked by %s.";

  // Discussions Administrator on mobile

  @Test(groups = "discussions-discussionsAdministratorUserMobileLocking")
  @Execute(asUser = User.DISCUSSIONS_ADMINISTRATOR)
  @InBrowser(browser = Browser.CHROME, emulator = Emulator.GOOGLE_NEXUS_5)
  public void discussionsAdministratorOnMobileCanLockPostOnPostDetailsPage() {
    PostEntity.Data data = DiscussionsOperations.using(User.USER, driver).cratePostWithUniqueData();

    PostEntity postEntity = lockPost(data);

    Assertion.assertTrue(postEntity.isLocked(), String.format(SHOULD_LOCK_MESSAGE, User.DISCUSSIONS_ADMINISTRATOR.name()));
  }

  @Test(groups = "discussions-discussionsAdministratorUserMobileLocking")
  @Execute(asUser = User.DISCUSSIONS_ADMINISTRATOR)
  @InBrowser(browser = Browser.CHROME, emulator = Emulator.GOOGLE_NEXUS_5)
  public void discussionsAdministratorOnMobileCanUnlockPostLockedByDiscussionsAdministratorOnPostDetailsPage() {
    PostEntity.Data data = DiscussionsOperations.using(User.USER, driver).cratePostWithUniqueData();

    PostEntity postEntity = unlockPost(data);

    final String name = User.DISCUSSIONS_ADMINISTRATOR.name();
    Assertion.assertFalse(postEntity.isLocked(), String.format(SHOULD_UNLOCK_MESSAGE, name, name));
  }

  // Staff user on mobile

  @Test(groups = "discussions-staffUserMobileLocking")
  @Execute(asUser = User.STAFF)
  @InBrowser(browser = Browser.CHROME, emulator = Emulator.GOOGLE_NEXUS_5)
  public void staffUserOnMobileCanLockPostOnPostDetailsPage() {
    PostEntity.Data data = DiscussionsOperations.using(User.USER, driver).cratePostWithUniqueData();

    PostEntity postEntity = lockPost(data);

    Assertion.assertTrue(postEntity.isLocked(), String.format(SHOULD_LOCK_MESSAGE, User.STAFF.name()));
  }

  @Test(groups = "discussions-staffUserMobileLocking")
  @Execute(asUser = User.STAFF)
  @InBrowser(browser = Browser.CHROME, emulator = Emulator.GOOGLE_NEXUS_5)
  public void staffUserOnMobileCanUnlockPostLockedByStaffOnPostDetailsPage() {
    PostEntity.Data data = DiscussionsOperations.using(User.USER, driver).cratePostWithUniqueData();
    DiscussionsOperations.using(User.STAFF, driver).lockPost(data);

    PostEntity postEntity = unlockPost(data);

    final String name = User.STAFF.name();
    Assertion.assertFalse(postEntity.isLocked(), String.format(SHOULD_UNLOCK_MESSAGE, name, name));
  }

  // Discussions moderator on mobile

  @Test(groups = "discussions-discussionsModeratorMobileLocking")
  @Execute(asUser = User.DISCUSSIONS_MODERATOR)
  @InBrowser(browser = Browser.CHROME, emulator = Emulator.GOOGLE_NEXUS_5)
  public void discussionsModeratorOnMobileCanNotAddReplyUnderLockedPostOnPostDetailsPage() {
    PostEntity.Data data = DiscussionsOperations.using(User.USER, driver).cratePostWithUniqueData();
    DiscussionsOperations.using(User.STAFF, driver).lockPost(data);

    PostDetailsPage page = new PostDetailsPage().open(data.getId());

    Assertion.assertFalse(page.getReplyCreatorMobile().isPresent(), "Discussions moderator should not be able to create reply under post locked by staff.");
  }


  @Test(groups = "discussions-discussionsModeratorMobileLocking")
  @Execute(asUser = User.DISCUSSIONS_MODERATOR)
  @InBrowser(browser = Browser.CHROME, emulator = Emulator.GOOGLE_NEXUS_5)
  public void discussionsModeratorOnMobileCanAddReplyUnderUnlockedPostOnPostDetailsPage() {
    PostEntity.Data data = DiscussionsOperations.using(User.USER, driver).cratePostWithUniqueData();
    DiscussionsOperations.using(User.STAFF, driver).lockPost(data)
        .unlockPost(data);

    final String text = TextGenerator.createUniqueText();

    PostDetailsPage page = new PostDetailsPage().open(data.getId());

    page.getReplyCreatorMobile().click()
        .clickGuidelinesReadButton()
        .add(text)
        .clickSubmitButton();

    boolean actual = page.getReplies()
        .waitForReplyToAppearWith(text)
        .isEmpty();

    Assertion.assertFalse(actual, "Discussions Moderator should be able to add reply to post unlocked by staff.");
  }

  private PostEntity lockPost(final PostEntity.Data data) {
    PostDetailsPage page = new PostDetailsPage().open(data.getId());
    final PostEntity postEntity = page.getPost().findNewestPost();
    postEntity.clickMoreOptions().clickLockPostOption();
    return postEntity;
  }

  private PostEntity unlockPost(final PostEntity.Data data) {
    PostDetailsPage page = new PostDetailsPage().open(data.getId());
    final PostEntity postEntity = page.getPost().findNewestPost();
    postEntity.clickMoreOptions().clickUnlockPostOption();
    return postEntity;
  }
}
