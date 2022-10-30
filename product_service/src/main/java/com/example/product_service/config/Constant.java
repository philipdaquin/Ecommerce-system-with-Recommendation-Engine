package com.example.product_service.config;

import lombok.NoArgsConstructor;

/**
 * NoArgsConstructor 
 * - will generate a constructor with no parameters 
 */
@NoArgsConstructor
public class Constant {
    /**
     * Public makes it accessible across other classes
     * Static makes it uniform value across all the class instances
     * final makes it non modifiable value
     * 
     * Constant Value which is same across all the class instances and cannot be modified
     */
    public static final String LOGIN_REGEX = "^(?>[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*)|(?>[_.@A-Za-z0-9-]+)$";

    public static final String DEFAULT_LANGUAGE = "en";
}
