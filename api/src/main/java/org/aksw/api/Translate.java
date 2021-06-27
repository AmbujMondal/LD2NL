package org.aksw.api;

class Translate {
    private String url;
    private String ce;
    private String nl;

    Translate() {
    }

    Translate(String url) {
        this.url = url;
    }

    Translate(String url, String ce) {
        this.url = url;
        this.ce = ce;
        SetNL();
    }

    public String GetUrl() {
        return this.url;
    }

    public String GetCE() {
        return this.ce;
    }

    public String GetNL() {
        return this.nl;
    }

    public void SetNL() {
        String nl = null;

        this.nl = nl;
    }
}
