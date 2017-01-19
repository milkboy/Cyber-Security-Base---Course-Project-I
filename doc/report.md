# Project report

### A1 Injection
The admin page has a method for showing a single signup, like
`http://localhost:8080/admin/1`. The id parameter is exploitable

### A3 Cross-Site Scripting
The admin page http://localhost:8080/admin renders the table with whatever raw data the
users have entered, including html tags.

##### Exploit
Usually, this should need a SQL injection or something, but the application
does not validate input before storing in the DB. Just enter
`<script>alert('pwned');</script>` as name or address for proof.

##### Fix
`th:utext` is inherently insecure, use `th:text` instead

### A10 Unvalidated Redirects and Forwards
After signing up, the user should have a link (or be automatically redirected)
to wherever he/she came from. The redirect parameter is used for this

##### Exploit
Simply add a redirect to the signup form, like:
`http://localhost:8080/form?redirect=http://google.fi`

##### Fix
