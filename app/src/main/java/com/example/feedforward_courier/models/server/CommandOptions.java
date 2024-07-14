package com.example.feedforward_courier.models.server;
/**
 * Enum representing various command options for searches.
 * <p>
 * The options include:
 * <ul>
 *   <li>{@link #SBRT} - Search by Radius and Type</li>
 *   <li>{@link #SBEC} - Search by Email and Created By</li>
 *   <li>{@link #SBECT} - Search by Email, Created By, and Type</li>
 *   <li>{@link #SBTA} - Search by Type and Alias</li>
 * </ul>
 */

public enum CommandOptions {
    /**
     * Search by Radius and Type.
     */
    SBRT, // Start BCI Recording

    /**
     * Search by Email and Created By.
     */
    SBEC,

    /**
     * Search by Email, Created By, and Type.
     */
    SBECT,

    /**
     * Search by Type and Alias.
     */
    SBTA
}
