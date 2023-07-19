package com.github.alwaysdarkk.booster.repository;

import com.github.alwaysdarkk.booster.data.Booster;
import com.github.alwaysdarkk.booster.repository.adapter.BoosterAdapter;
import com.henryfabio.sqlprovider.executor.SQLExecutor;
import lombok.RequiredArgsConstructor;

import java.util.Set;
import java.util.concurrent.CompletableFuture;

@RequiredArgsConstructor
public class BoosterRepository {

    private final SQLExecutor sqlExecutor;

    public void createTable() {
        final String QUERY =
                "CREATE TABLE IF NOT EXISTS boosters(owner VARCHAR(16) NOT NULL PRIMARY KEY, skillType TEXT NOT NULL, expireAt LONG NOT NULL)";
        sqlExecutor.updateQuery(QUERY);
    }

    public void insertOne(Booster booster) {
        final String QUERY = "REPLACE INTO boosters VALUES(?, ?, ?)";
        CompletableFuture.runAsync(() -> sqlExecutor.updateQuery(QUERY, simpleStatement -> {
            simpleStatement.set(1, booster.getOwner());
            simpleStatement.set(2, booster.getSkillType().name());
            simpleStatement.set(3, booster.getExpireAt());
        }));
    }

    public void deleteOne(Booster booster) {
        final String QUERY = "DELETE FROM boosters WHERE owner = ?";
        CompletableFuture.runAsync(
                () -> sqlExecutor.updateQuery(QUERY, simpleStatement -> simpleStatement.set(1, booster.getOwner())));
    }

    public Booster selectOne(String owner) {
        final String QUERY = "SELECT * FROM boosters WHERE owner = ?";
        return sqlExecutor.resultOneQuery(
                QUERY, simpleStatement -> simpleStatement.set(1, owner), BoosterAdapter.class);
    }

    public Set<Booster> selectAll() {
        final String QUERY = "SELECT * FROM boosters";
        return sqlExecutor.resultManyQuery(QUERY, simpleStatement -> {}, BoosterAdapter.class);
    }
}