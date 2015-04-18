# Synca

##Why
I reached a point where I thought that anything of interest on my Android phone will somehow magically backed up somewhere in the cloud.

I was wrong.

*WhatsApp* content wasn't. There are plenty of tools out there that back stuff up for you but I wanted a simpler one. That's why I started writing Synca. All it does at the moment is looking for the *WhatsApp* folder and backing up images to the apps *DropBox* folder. That's it.

##How
There might be a smarter way to put your secrets into an open source project, I haven't found it yet. If you want to reuse this app you can put your *Dropbox* secrets in a resource file under `\app\res\values`:

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="APP_KEY">your app key</string>
    <string name="APP_SECRET">your secret</string>
</resources>
```
