package org.gooru.navigatemap.responses.auth;

public interface AuthResponseHolder {
    boolean isAuthorized();

    boolean isAnonymous();

    String getUser();
}
