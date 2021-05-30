![VertLibrary](./img/vertlibrary.png)

These instructions will learn you how to set use this library.

### Depending on the repository.

---

> (Step 1): Add the VertCode Development Repository.

```xml

<repository>
    <id>vertcode-development</id>
    <url>https://repo.vertcode.dev/repository/vertcodedevelopment/</url>
</repository>
```

> (Step 2): Adding the dependency.

```xml
<dependency>
    <groupId>dev.vertcode.vertlibrary</groupId>
    <artifactId>{type}</artifactId>
    <version>{version}</version>
    <scope>provided</scope>
</dependency>
```

> (Step 4): Replace {type} with **commons**, **spigot** or **velocity**

> (Step 5): Replace {version} with the version you want to use

---

### Commands (Minecraft):

```yaml
/vertlibrary - Sends information about the library / opens the gui.
```

### Support / Docs

Discord » https://vertcode.dev/discord

Documentation » https://docs.vertcodedevelopment.com/vertlibrary 