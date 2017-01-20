# Project report
Vulnerability (and fix suggestion) report for _Cyber Security Base - Course Project I_.

### A1 Injection
The admin page has a method for showing a single signup, like
`http://localhost:8080/admin/1`. The "1" equals the id parameter
and is easily exploitable.
The exploitability is worsened by the fact that this page is world accessible
without authentication. 

##### Exploit
https://github.com/sqlmapproject/sqlmap can be used for verification.
like `python sqlmap.py -u "http://localhost:8080/admin/1*" -b --dbms=hsqldb`
##### Fix
Don't use "dynamic" SQL queries. EVER. If really needed, any JPA query can be specified
and parameterized as needed, and the parameters will be automatically escaped properly.
The whole custom signup repository is unneeded, as the logic should be in the controller.

### A3 Cross-Site Scripting
The admin page http://localhost:8080/admin renders the table with whatever raw data the
users have entered, including html tags.

##### Exploit
Usually, this should need a SQL injection or something, but the application
does not validate input before storing in the DB. Just enter
`<script>alert('pwned');</script>` as name or address for proof.

##### Fix
`th:utext` is insecure by design, use `th:text` instead

### A5 Security misconfiguration
Not all admin pages are properly secured.

##### Exploit
`http://localhost:8080/admin/1` can be accessed without authentication.

##### Fix
`.antMatchers("/admin", "/admin/").authenticated()` should probably more like
`.antMatchers("/admin*").authenticated()`, or more preferrably, explicitly state which pages
are ok to access unauthenticated, and then require authentication (and authorization also!) for everything else.

### A6 Sensitive Data Exposure
The "very secure" password hash (and all other details also) of any registered 
user are leaked through a very easy to guess URL.

##### Exploit
Since all user Ids are sequential numbers, it's not hard to brute force all of them.
The page will alert that the password does not match, but all the details are
still visible by looking at the code. Sample `http://localhost:8080/event/1/whatever`

##### Fix
The password is to be verified on server side. Also, sending a password (even if it's just
some generated passcode for a single event) in the url is a **BAD** idea. If the page url is
supposed to be accessible without any authentication, it should probably have some GUID or something;
`http://localhost:8080/event/0878fb26-c84a-48eb-9f68-c1eefc2223c7`

### A10 Unvalidated Redirects and Forwards
After signing up, the user should have a link (or be automatically redirected)
to wherever he/she came from. The redirect parameter is used for this

##### Exploit
Simply add a redirect to the signup form, like:
`http://localhost:8080/form?redirect=http://google.fi`

##### Fix
Redirect parameters should usually not be used. If the need actually arises, the redirect
should only contain a relative path, preferably without query parameters, and it should be
validated before being used.
