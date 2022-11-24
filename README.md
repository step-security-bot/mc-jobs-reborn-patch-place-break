# jobs-reborn-patch-place-break

![Target](https://img.shields.io/badge/plugin-Minecraft-blueviolet)
![Minecraft version](https://img.shields.io/badge/version-1.11%20--%201.19-blue)
[![CircleCI](https://dl.circleci.com/status-badge/img/gh/Djaytan/mc-jobs-reborn-patch-place-break/tree/main.svg?style=svg)](https://dl.circleci.com/status-badge/redirect/gh/Djaytan/mc-jobs-reborn-patch-place-break/tree/main)

A place-and-break patch extension of [JobsReborn plugin](https://www.spigotmc.org/resources/jobs-reborn.4216/)
for Bukkit servers.

The resource is available on [Spigot](https://www.spigotmc.org/resources/jobsreborn-patchplacebreak.102779/).

## Place-and-break issue

With JobsReborn, it appears that placing a block and then breaking it is counted as a valid job
action which lead to a payment for the player. Given this fact, it's very easy to imagine a
diamond ore to be gathered with a Silk Touch pickaxe and immediately after be replaced to
repeat the process again and again...

A solution with JobsReborn is to remove money and xp when a diamond ore is placed, preventing
the previously described scenario. But it isn't perfect: if you expect to use money and xp boost
for whatever reason, the amount of money to give when the block is broken will be higher than
the amount to be retrieved when placing the same one. And... well... losing money and xp when you
place a block for decoration isn't very appreciated by players too.

The place-and-break patch provided by JobsReborn seems to be insufficient: you must specify **for each
block** a fix amount of time during which breaking the block again will not permit to earn money
and xp. It's a first step forward, but can go even further. Specifying an unlimited time can be limited too,
because after 14 days maximum the placed blocks will not prevent the payment
anymore... And finally, piston exploit isn't taking into account at all.

So, this is why this addon exists: giving an easy and efficient solution to these problematics.

## How the patch works

The patch is very simple: when breaking or placing blocks, each one is tagged. This information
is persisted across server restarts.

At payment time, if a BREAK, TNTBREAK or PLACE action involve an active "player" tag, the payment
will be cancelled. It doesn't matter whose player is the author, so if one player place a block
and another one break it, the payment will be cancelled anyway.

There are two main behaviors which have subtle differences:

* When a block is placed, a tag is attached to it: This is useful to patch BREAK and TNTBREAK
  actions (e.g. for breaking diamond ores) ;
* When a block is broken, a tag is attached to the location where it was: This is useful to
  patch PLACE actions (e.g. for placing saplings).

*Note: the second behavior lead to "ephemeral" tags, that's to say a tag which will be
applicable during a short-time only. The value is fixed to three seconds.*

As a comparison point, this behavior can have similarities with the one implemented by
[mcMMO](https://www.spigotmc.org/resources/official-mcmmo-original-author-returns.64348/) plugin.

Easy and efficient, this does the trick.

## Setup

We expect here that you already have a Bukkit server already set up with the JobsReborn plugin
installed on it.

The server's version must be higher or equals to 1.11. If you wish to use this plugin on a lower version of the server
(1.8, 1.9 or 1.10), you should instead use the version
[1.2.0 of the plugin](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/releases/tag/v1.2.0).

Simply download the latest `.jar` file from the
[release section](https://github.com/Djaytan/mc-jobs-reborn-patch-place-break/releases/) of this
repository and put it into the `plugins/` folder, and you'll be done! After restarting the server,
the plugin should now appear green in the list displayed by the `/plugins` command.

At this point, you should simply turn off all options of the "PlaceAndBreak" config part of JobsReborn.
This would lead to a similar result as the following one:

```yaml
PlaceAndBreak:
  # Enable blocks protection, like ore, from exploiting by placing and destroying same block again and again.
  # Modify restrictedBlocks.yml for blocks you want to protect
  Enabled: false
  # Enabling this we will ignore blocks generated in ore generators, liko stone, coublestone and obsidian. You can still use timer on player placed obsidian block
  IgnoreOreGenerators: true
  # For how long in days to keep block protection data in data base
  # This will clean block data which ones have -1 as cooldown value
  # Data base cleanup will be performed on each server startup
  # This cant be more then 14 days
  KeepDataFor: 14
  # All blocks will be protected X sec after player places it on ground.
  GlobalBlockTimer:
    Use: false
    Timer: 3
  # Enable silk touch protection.
  # With this enabled players wont get paid for broken blocks from restrictedblocks list with silk touch tool.
  SilkTouchProtection: false
```

## How to compile the project

Since a [Maven Wrapper](https://github.com/apache/maven-wrapper) has been set up for this project, it's recommended
to use it instead of your own local instance by executing the script `mvnw`
(or `mvnw.cmd` if you are under Windows terminal of Powershell).

To compile the project, run unit tests and package the plugin into a Jar file, simply run this command
without any arguments: `mvnw`
(see [Maven lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html) for more
details).
This is equivalent to `mvnw clean install`.

## Remaining work to do

Actually, an existing and known issue is the size of the JAR file which is around ~17 Mo. This is explained by the use
of libraries and tools among which some of them are particularly heavy without really reasons (e.g. SQLite client which
take more than the half of the JAR file). I'm currently working on fixing this.

## Licence

This project is under the licence [GNU GPLv3](https://www.gnu.org/licenses/gpl-3.0.en.html).
