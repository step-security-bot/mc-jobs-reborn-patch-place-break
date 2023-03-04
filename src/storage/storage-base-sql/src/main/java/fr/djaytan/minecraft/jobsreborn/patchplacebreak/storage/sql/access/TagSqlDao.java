/*
 * The MIT License
 * Copyright © 2022 Loïc DUBOIS-TERMOZ (alias Djaytan)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.access;

import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.BlockLocation;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.api.entities.Tag;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.api.properties.DataSourceProperties;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.BooleanIntegerSerializer;
import fr.djaytan.minecraft.jobsreborn.patchplacebreak.storage.sql.serializer.LocalDateTimeStringSerializer;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Optional;
import javax.inject.Inject;
import javax.inject.Singleton;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Singleton
public class TagSqlDao {

  private final BooleanIntegerSerializer booleanIntegerSerializer;
  private final DataSourceProperties dataSourceProperties;
  private final LocalDateTimeStringSerializer localDateTimeStringSerializer;

  @Inject
  public TagSqlDao(
      BooleanIntegerSerializer booleanIntegerSerializer,
      DataSourceProperties dataSourceProperties,
      LocalDateTimeStringSerializer localDateTimeStringSerializer) {
    this.booleanIntegerSerializer = booleanIntegerSerializer;
    this.dataSourceProperties = dataSourceProperties;
    this.localDateTimeStringSerializer = localDateTimeStringSerializer;
  }

  public void insert(@NonNull Connection connection, @NonNull Tag tag) throws SQLException {
    String sql =
        String.format("INSERT INTO %s VALUES (?, ?, ?, ?, ?, ?)", dataSourceProperties.getTable());

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, tag.getBlockLocation().getWorldName());
      preparedStatement.setInt(2, tag.getBlockLocation().getX());
      preparedStatement.setInt(3, tag.getBlockLocation().getY());
      preparedStatement.setInt(4, tag.getBlockLocation().getZ());
      preparedStatement.setInt(5, booleanIntegerSerializer.serialize(tag.isEphemeral()));
      preparedStatement.setString(
          6, localDateTimeStringSerializer.serialize(tag.getInitLocalDateTime()));
      preparedStatement.executeUpdate();
    }
  }

  public void updateLocation(
      @NonNull Connection connection,
      @NonNull BlockLocation oldLocation,
      @NonNull BlockLocation newLocation)
      throws SQLException {
    String sql =
        String.format(
            "UPDATE %s "
                + "SET world_name = ?, location_x = ?, location_y = ?, location_z = ? "
                + "WHERE world_name = ? AND location_x = ? AND location_y = ? AND location_z = ?",
            dataSourceProperties.getTable());

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, newLocation.getWorldName());
      preparedStatement.setInt(2, newLocation.getX());
      preparedStatement.setInt(3, newLocation.getY());
      preparedStatement.setInt(4, newLocation.getZ());
      preparedStatement.setString(5, oldLocation.getWorldName());
      preparedStatement.setInt(6, oldLocation.getX());
      preparedStatement.setInt(7, oldLocation.getY());
      preparedStatement.setInt(8, oldLocation.getZ());
      preparedStatement.executeUpdate();
    }
  }

  public @NonNull Optional<Tag> findByLocation(
      @NonNull Connection connection, @NonNull BlockLocation blockLocation) throws SQLException {
    String sql =
        String.format(
            "SELECT * FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
                + " location_z = ?",
            dataSourceProperties.getTable());

    try (PreparedStatement preparedStatement = connection.prepareStatement(sql)) {
      preparedStatement.setString(1, blockLocation.getWorldName());
      preparedStatement.setInt(2, blockLocation.getX());
      preparedStatement.setInt(3, blockLocation.getY());
      preparedStatement.setInt(4, blockLocation.getZ());

      try (ResultSet resultSet = preparedStatement.executeQuery()) {
        return extractTag(resultSet);
      }
    }
  }

  private @NonNull Optional<Tag> extractTag(@NonNull ResultSet resultSet) throws SQLException {
    if (resultSet.getFetchSize() > 1) {
      log.atWarn()
          .log(
              "Multiple tags detected for a same location, selecting the first one."
                  + " Anyway, please report this issue to the developer for investigation.");
    }

    if (!resultSet.next()) {
      return Optional.empty();
    }

    String worldName = resultSet.getString("world_name");
    int x = resultSet.getInt("location_x");
    int y = resultSet.getInt("location_y");
    int z = resultSet.getInt("location_z");
    BlockLocation blockLocation = BlockLocation.of(worldName, x, y, z);

    boolean isEphemeral = booleanIntegerSerializer.deserialize(resultSet.getInt("is_ephemeral"));
    LocalDateTime initLocalDateTime =
        localDateTimeStringSerializer.deserialize(resultSet.getString("init_timestamp"));

    Tag tag = Tag.of(blockLocation, isEphemeral, initLocalDateTime);
    return Optional.of(tag);
  }

  public void delete(@NonNull Connection connection, @NonNull BlockLocation blockLocation)
      throws SQLException {
    String sqlDelete =
        String.format(
            "DELETE FROM %s WHERE world_name = ? AND location_x = ? AND location_y = ? AND"
                + " location_z = ?",
            dataSourceProperties.getTable());

    try (PreparedStatement deleteStmt = connection.prepareStatement(sqlDelete)) {
      deleteStmt.setString(1, blockLocation.getWorldName());
      deleteStmt.setInt(2, blockLocation.getX());
      deleteStmt.setInt(3, blockLocation.getY());
      deleteStmt.setInt(4, blockLocation.getZ());
      deleteStmt.executeUpdate();
    }
  }
}
