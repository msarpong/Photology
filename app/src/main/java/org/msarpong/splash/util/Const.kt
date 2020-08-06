package org.msarpong.splash.util

const val BASE_URL = "https://api.unsplash.com/"
const val AUTH_URL = "https://unsplash.com/oauth/authorize/"
const val TOKEN_URL = "https://unsplash.com/oauth/"
const val REDIRECT_URI = "urn:ietf:wg:oauth:2.0:oob"


const val CLIENT_ID = "cf9190b65fe2f5ad324ce4507dacbf926e7d819e996e502e6e51b72b88f1472a"
const val CLIENT_SECRET = "dcc0502f57b3ef26be3acf195ccc677dd7689548c63011beb2b86a063d1ae04f"

const val SCOPE = "public+write_user+read_user"
const val GRANT_TYPE = "authorization_code"
const val RESPONSE_TYPE = "code"
const val ACCESS_TOKEN = "access_token"

const val url =
    "$AUTH_URL?client_id=$CLIENT_ID&redirect_uri=$REDIRECT_URI&scope=$SCOPE&response_type=$RESPONSE_TYPE"
const val MY_PERMISSIONS_REQUEST_WRITE_EXTERNAL_STORAGE = 1

const val IS_LOGGED = "is_logged"
const val USERNAME = "username"
const val ID_USER = "id_user"
