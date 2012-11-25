package kr.co.talesapp.clouduapi;

public class OAuthTokens {
	private String oauth_token="";
	private String oauth_token_secret="";
	private String access_token="";
	private String consumer_key="";
	private String consumer_secret="";
	private String ticket="";
	
	public void setOAuthToken(String oauth_token) {
		this.oauth_token=oauth_token;
	}
	public String getOAuthToken() {
		return oauth_token;
	}
	public void setOAuthTokenSecret(String oauth_token_secret) {
		this.oauth_token_secret=oauth_token_secret;
	}
	public String getOAuthTokenSecret() {
		return oauth_token_secret;
	}
	public void setAccessToken(String access_token) {
		this.access_token=access_token;
	}
	public String getAccessToken() {
		return access_token;
	}
	public void setTicket(String ticket) {
		this.ticket=ticket;
	}
	public String getTicket() {
		return ticket;
	}
	public void setConsumerKey(String consumer_key) {
		this.consumer_key=consumer_key;
	}
	public String getConsumerKey() {
		return consumer_key;
	}
	public void setConsumerSecret(String consumer_secret) {
		this.consumer_secret=consumer_secret;
	}
	public String getConsumerSecret() {
		return consumer_secret;
	}
}
