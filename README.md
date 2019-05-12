# callerid

## History
Years ago I thought it would be quite cool and useful to receive some sort of notification or alert when my landline rang to display the number and perhaps the caller's name.  Having caller id as a feature on my landline that the handset can display meant that the number must be  [pushed down the line somehow](https://en.wikipedia.org/wiki/Caller_ID) so I spent some time back in the 90s searching for old modems (US Robotics from memory) that supported the feature but I had limited success.  In the early 2000s I bought an old first generation TiVo that was hackable and found that one of the hacks was to display messages as banners on the TV which sounded like a good use for displaying who was calling when watching TV.

Around about the same time I was also playing with VOIP and wanted to make that seamless with the landline phones so I bought a [Sipura box](https://www.voip-info.org/sipura-3000/) that the landline and ethernet port plugged into on one side and the phone on the other.  The idea being that both VOIP and regular calls could be handled by the landline handset.  Interesting idea but it ultimately failed the _wife test_ (echo, delay, tones different) so back in the drawer it went for a few months.

Sometime later and I can't remember why I had a eureka moment when I remembered that the Sipura could log to a syslog server and it seemed likely that the inbound callerid would be logged.  Turns out that lots of call and line data is logged but the interesting entry is the calling number prefixed with `FXO`.

Currently my Sipura box, now at least fourteen or fifteen years old, sits quite happily plugged into the landline and router in debug mode set to verbose and logs to an app listening on the standard syslog port on a Synology NAS.  The app does all the work or parsing the log events and looking up the inbound number in an address book so a known caller's name is displayed instead.

I have a few implementations that use for picking up a new language or framework.  The legacy version is a simple Java application [simple-callerid](https://github.com/eharrow/callerid/tree/master/java-callerid).  In addition I have an Apache Camel (therefore Spring) version that is really compact and neat but dies on my Synology NAS and has about a 2 minute start up time.  Currently I am working on a javascript version for a Node.js runtime.

Notes on [exporting a mac address book](https://blog.ewanharrow.com/2019/05/11/export-mac-addressbook/).
