package com.vaadin.starter.bakery.ui.views.orderedit;

import java.time.LocalDateTime;

import com.vaadin.starter.bakery.backend.data.OrderState;
import com.vaadin.starter.bakery.backend.data.entity.HistoryItem;
import com.vaadin.starter.bakery.backend.data.entity.User;
import com.vaadin.starter.bakery.ui.utils.converters.LocalDateTimeConverter;

/**
 * Read-only DTO for displaying history item data in the UI.
 */
public class HistoryItemDisplayData {

    private final Long id;
    private final int version;
    private final OrderState newState;
    private final String message;
    private final LocalDateTime timestamp;
    private final User createdBy;
    private final String formattedTimestamp;

    private HistoryItemDisplayData(Long id, int version, OrderState newState, String message,
                                   LocalDateTime timestamp, User createdBy, String formattedTimestamp) {
        this.id = id;
        this.version = version;
        this.newState = newState;
        this.message = message;
        this.timestamp = timestamp;
        this.createdBy = createdBy;
        this.formattedTimestamp = formattedTimestamp;
    }

    public static HistoryItemDisplayData fromHistoryItem(HistoryItem historyItem) {
        return new HistoryItemDisplayData(
                historyItem.getId(),
                historyItem.getVersion(),
                historyItem.getNewState(),
                historyItem.getMessage(),
                historyItem.getTimestamp(),
                historyItem.getCreatedBy(),
                new LocalDateTimeConverter().encode(historyItem.getTimestamp())
        );
    }

    public Long getId() {
        return id;
    }

    public int getVersion() {
        return version;
    }

    public OrderState getNewState() {
        return newState;
    }

    public String getMessage() {
        return message;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public User getCreatedBy() {
        return createdBy;
    }

    public String getFormattedTimestamp() {
        return formattedTimestamp;
    }
}
