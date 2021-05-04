package org.ionproject.core.common.customExceptions

import java.lang.RuntimeException

class EnvironmentVariableNotFoundException(
    envVar: String
) : RuntimeException("Please define the $envVar environment variable")
