import {BrowserPage} from '../page/BrowserPage.po';
import {AppPage} from "../page/AppPage.po";
import {logging} from "selenium-webdriver";

describe('Smoke Tests', function () {

    let appPage: AppPage;

    beforeAll(async function () {
        new BrowserPage().openApp()
            .then(page => page.authPage().loginWithAuthKeyIfNeeded())
            .then(page => appPage = page);
    });

    afterAll(async function () {
        appPage.browserPage()
            .consolePage()
            .getBrowserLogs()
            .then(logs =>
                logs
                    .keepLevels(logging.Level.SEVERE)
                    .removeMessage("/api/authentication/user")
                    .removeMessage("/login")
                    .removeMessage("/main-es2015.js")
                    .removeMessage("/vendor-es2015.js")
                    .assertNoLog()
            );
    });

    it('should land on default route', function () {
    });

    it('should navigate to admin', function () {
        appPage.menuPage().clickOnAdminItem();
    });

    it('should navigate to settings', function () {
        appPage.menuPage().clickOnSettingsItem();
    });

    it('should navigate to translations', function () {
        appPage.menuPage().clickOnTranslationsItem();
    });

});