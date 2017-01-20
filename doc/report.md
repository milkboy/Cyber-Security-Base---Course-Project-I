# Project report
Vulnerability (and fix suggestion) report for _Cyber Security Base - Course Project I_.

To run the project, use either
- maven: `mvn spring-boot:run` and go to `http://localhost:8080/`
- deploy WAR on tomcat/GlassFish/...: `mvn package` produces the WAR in target/

Oh, and the admin username is "ted", and password "ted".

### A1 Injection
The admin page has a method for showing a single signup, like
`http://localhost:8080/admin/1`. The "1" equals the id parameter
and is easily exploitable (blind sql injection at least).
The exploitability is worsened by the fact that this page is world accessible
without authentication. 

##### Exploit
https://github.com/sqlmapproject/sqlmap can be used for verification.
Like so: `python sqlmap.py -u "http://localhost:8080/admin/1*" -b` to get the
database version string:

~~~~
URI parameter '#1*' is vulnerable. Do you want to keep testing the others (if any)? [y/N]
sqlmap identified the following injection point(s) with a total of 64 HTTP(s) requests:
---
Parameter: #1* (URI)
  Type: boolean-based blind
  Title: AND boolean-based blind - WHERE or HAVING clause
  Payload: http://localhost:8080/admin/1' AND 9007=9007 AND 'zfcO'='zfcO
---
[11:50:28] [INFO] retrieved: 2.3.3
back-end DBMS: HSQLDB = 2.3.3
banner:    '2.3.3'
~~~~

##### Fix
Don't use "dynamic" SQL queries. EVER. If really needed, any JPA query can be specified
and parametrized as needed, and the parameters will be automatically escaped properly.
The whole custom signup repository is unneeded, as the logic should be in the controller.

####Bonus
Also the signup form uses dynamic SQL


### A3 Cross-Site Scripting
The admin page(s) `http://localhost:8080/admin` renders the table with whatever raw data the
users have entered, including html tags.

##### Exploit
Usually, this should need a SQL injection or something, but the application
does not validate input before storing in the DB nor before displaying it. Just enter
`<script>alert("pwned");</script>` as name or address for proof. (Remember that single quotes
will cause SQL statement errors ;)
Results are visible on the admin page.

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
