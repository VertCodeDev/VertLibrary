package dev.vertcode.vertlibrary.mongo;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class MongoDBSettings {

    private final String connectionUri, databaseName;
}
