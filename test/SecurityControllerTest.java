/*
 * Modified from https://github.com/jamesward/play-rest-security
 */

package controllers;

import com.avaje.ebean.Ebean;
import com.avaje.ebean.EbeanServer;
import com.avaje.ebean.EbeanServerFactory;
import com.avaje.ebean.config.ServerConfig;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import com.google.common.collect.ImmutableMap;
import models.SwipingSession;
import models.User;
import org.junit.After;
import org.junit.ClassRule;
import org.junit.Test;
import org.junit.Before;

import play.Application;
import play.libs.Json;
import play.mvc.Result;
import play.test.WithApplication;
import utils.DemoData;

import play.db.Database;

import javax.inject.Inject;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static play.mvc.Http.Status.OK;
import static play.test.Helpers.*;

import play.Logger;

public class SecurityControllerTest extends WithApplication {

    private Database db;
    private DemoData demoData;
    private ObjectNode loginJson;

    public void setup() {

        demoData = app.injector().instanceOf(DemoData.class);
        loginJson = Json.newObject();

    }

    @Test
    public void login() {

        setup();
        loginJson.put("emailAddress", demoData.user1.getEmailAddress());
        loginJson.put("password", demoData.user1.getPassword());

        Result result = route(fakeRequest(controllers.routes.SecurityController.login()).bodyJson(loginJson));

        assertEquals(OK, result.status());

        JsonNode json = Json.parse(contentAsString(result));
        assertNotNull(json.get("authToken"));

    }

    /*@Test
    public void loginWithBadPassword() {

        setup();
        loginJson.put("emailAddress", demoData.user1.getEmailAddress());
        loginJson.put("password", demoData.user1.getPassword().substring(1));

        Result result = route(fakeRequest(controllers.routes.SecurityController.login()).bodyJson(loginJson));
        assertEquals(UNAUTHORIZED, result.status());

    }

    @Test
    public void loginWithBadUsername() {

        setup();
        loginJson.put("emailAddress", demoData.user1.getEmailAddress().substring(1));
        loginJson.put("password", demoData.user1.getPassword());

        Result result = route(fakeRequest(controllers.routes.SecurityController.login()).bodyJson(loginJson));
        assertEquals(UNAUTHORIZED, result.status());
        
    }

    @Test
    public void loginWithDifferentCaseUsername() {

        setup();
        loginJson.put("emailAddress", demoData.user1.getEmailAddress().toUpperCase());
        loginJson.put("password", demoData.user1.getPassword());

        Result result = route(fakeRequest(controllers.routes.SecurityController.login()).bodyJson(loginJson));
        assertEquals(OK, result.status());

    }

    @Test
    public void loginWithNullPassword() {
        
        setup();
        loginJson.put("emailAddress", demoData.user1.getEmailAddress());

        Result result = route(fakeRequest(controllers.routes.SecurityController.login()).bodyJson(loginJson));
        assertEquals(BAD_REQUEST, result.status());

    }

    @Test
    public void logout() {
        
        demoData = app.injector().instanceOf(DemoData.class);
        String authToken = demoData.user1.createToken();

        Result result = route(fakeRequest(controllers.routes.SecurityController.logout()).header(SecurityController.AUTH_TOKEN_HEADER, authToken));
        assertEquals(SEE_OTHER, result.status());

    }*/
}



