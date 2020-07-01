package org.ionproject.core.common.customExceptions

class ForeignKeyException(entity1: String, entity2: String) :
    IllegalStateException("Exists a foreign key constraint violation between $entity1 and $entity2")