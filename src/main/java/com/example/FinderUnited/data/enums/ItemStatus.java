package com.example.FinderUnited.data.enums;

public enum ItemStatus {
    LOST,    // Item is lost
    FOUND,   // Item is found but not claimed yet
    CLAIMED, // Item has been claimed by the owner
    RETURNED // Item has been returned to the owner
}
