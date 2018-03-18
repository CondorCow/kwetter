/*
 * Copyright (c) 2018.
 * Danny Janssen
 */

package support;

public class ResponseBody {
    private boolean response;
    private String message;

    public ResponseBody(boolean response, String message) {
        this.response = response;
        this.message = message;
    }

    public boolean getResponse() {
        return response;
    }

    public void setResponse(boolean response) {
        this.response = response;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
