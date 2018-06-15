package com.yuanin.aimifinance.entity;

public class LoginNewEntity {

    /**
     * userInfo : {"created":1528337798,"email":"15090602313@","emailValid":0,"lastIpLogged":"1961024346","lastTimeLogged":1528859032000,"loginToken":"8dabb248e9164825ab7ccd0fdd8e44db","mobile":"15090602313","mobileValid":1,"page":1,"password":"0507231f7e770e14a12702d7c3515d79","role":0,"rows":10,"salt":"C2vrAK","status":"allow","uid":66730}
     * mobile : 15090602313
     * login_token : 8dabb248e9164825ab7ccd0fdd8e44db
     * userid : 66730
     */

    private UserInfo userInfo;
    private String mobile;
    private String login_token;
    private String userid;

    public UserInfo getUserInfo() {
        return userInfo;
    }

    public void setUserInfo(UserInfo userInfo) {
        this.userInfo = userInfo;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getLogin_token() {
        return login_token;
    }

    public void setLogin_token(String login_token) {
        this.login_token = login_token;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    private class UserInfo {


        /**
         * created : 1528337798
         * email : 15090602313@
         * emailValid : 0
         * lastIpLogged : 1961024346
         * lastTimeLogged : 1528859032000
         * loginToken : 8dabb248e9164825ab7ccd0fdd8e44db
         * mobile : 15090602313
         * mobileValid : 1
         * page : 1
         * password : 0507231f7e770e14a12702d7c3515d79
         * role : 0
         * rows : 10
         * salt : C2vrAK
         * status : allow
         * uid : 66730
         */

        private int created;
        private String email;
        private int emailValid;
        private String lastIpLogged;
        private long lastTimeLogged;
        private String loginToken;
        private String mobile;
        private int mobileValid;
        private int page;
        private String password;
        private int role;
        private int rows;
        private String salt;
        private String status;
        private int uid;

        public int getCreated() {
            return created;
        }

        public void setCreated(int created) {
            this.created = created;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public int getEmailValid() {
            return emailValid;
        }

        public void setEmailValid(int emailValid) {
            this.emailValid = emailValid;
        }

        public String getLastIpLogged() {
            return lastIpLogged;
        }

        public void setLastIpLogged(String lastIpLogged) {
            this.lastIpLogged = lastIpLogged;
        }

        public long getLastTimeLogged() {
            return lastTimeLogged;
        }

        public void setLastTimeLogged(long lastTimeLogged) {
            this.lastTimeLogged = lastTimeLogged;
        }

        public String getLoginToken() {
            return loginToken;
        }

        public void setLoginToken(String loginToken) {
            this.loginToken = loginToken;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public int getMobileValid() {
            return mobileValid;
        }

        public void setMobileValid(int mobileValid) {
            this.mobileValid = mobileValid;
        }

        public int getPage() {
            return page;
        }

        public void setPage(int page) {
            this.page = page;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public int getRole() {
            return role;
        }

        public void setRole(int role) {
            this.role = role;
        }

        public int getRows() {
            return rows;
        }

        public void setRows(int rows) {
            this.rows = rows;
        }

        public String getSalt() {
            return salt;
        }

        public void setSalt(String salt) {
            this.salt = salt;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public int getUid() {
            return uid;
        }

        public void setUid(int uid) {
            this.uid = uid;
        }
    }
}
