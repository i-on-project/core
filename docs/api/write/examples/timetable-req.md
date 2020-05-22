# Example Timetable Request
```json
{
    "school": {
        "name": "INSTITUTO SUPERIOR DE ENGENHARIA DE LISBOA",
        "acr": "ISEL"
    },
    "programme": {
        "name": "Licenciatura Engenharia Informática e Computadores"
    },
    "calendarSection": "LI11D",
    "courses": [
        {
            "acr": "ALGA[I]",
            "timetable": [
                {
                    "type": "t",
                    "location": "E.1.31",
                    "beginTime": "14:00:00",
                    "endTime": "15:30:00",
                    "duration": "1:30:00",
                    "weekday": [ "Monday" ]
                }
            ]
        },
        {
            "acr": "M2[I]",
            "name": "Matemática II",
            "timetable": [
                {
                    "type": "t",
                    "location": "G.2.30",
                    "beginTime": "14:00:00",
                    "endTime": "15:30:00",
                    "duration": "1:30:00",
                    "weekday": [ "Tuesday" ]
                },
                {
                    "location": "G.2.30",
                    "beginTime": "14:00:00",
                    "endTime": "15:30:00",
                    "duration": "1:30:00",
                    "weekday": [ "Friday" ]
                }
            ]
        }
    ]
}
```

# Timetable Request's JSON Schema
```json
{
    "$schema": "http://json-schema.org/draft-07/schema",
    "$id": "http://example.com/example.json",
    "type": "object",
    "title": "The root schema",
    "description": "The root schema comprises the entire JSON document.",
    "default": {},
    "examples": [
        {
            "school": {
                "name": "INSTITUTO SUPERIOR DE ENGENHARIA DE LISBOA",
                "acr": "ISEL"
            },
            "programme": {
                "name": "Licenciatura Engenharia Informática e Computadores"
            },
            "calendarSection": "LI11D",
            "courses": [
                {
                    "acr": "ALGA[I]",
                    "timetable": [
                        {
                            "type": "t",
                            "location": "E.1.31",
                            "beginTime": "14:00:00",
                            "endTime": "15:30:00",
                            "duration": "1:30:00",
                            "weekday": [
                                "Monday"
                            ]
                        }
                    ]
                },
                {
                    "acr": "M2[I]",
                    "name": "Matemática II",
                    "timetable": [
                        {
                            "type": "t",
                            "location": "G.2.30",
                            "beginTime": "14:00:00",
                            "endTime": "15:30:00",
                            "duration": "1:30:00",
                            "weekday": [
                                "Tuesday"
                            ]
                        },
                        {
                            "location": "G.2.30",
                            "beginTime": "14:00:00",
                            "endTime": "15:30:00",
                            "duration": "1:30:00",
                            "weekday": [
                                "Friday"
                            ]
                        }
                    ]
                }
            ]
        }
    ],
    "required": [
        "school",
        "programme",
        "calendarSection",
        "courses"
    ],
    "additionalProperties": true,
    "properties": {
        "school": {
            "$id": "#/properties/school",
            "type": "object",
            "title": "The school schema",
            "description": "An explanation about the purpose of this instance.",
            "default": {},
            "examples": [
                {
                    "name": "INSTITUTO SUPERIOR DE ENGENHARIA DE LISBOA",
                    "acr": "ISEL"
                }
            ],
            "required": [
                "name",
                "acr"
            ],
            "additionalProperties": true,
            "properties": {
                "name": {
                    "$id": "#/properties/school/properties/name",
                    "type": "string",
                    "title": "The name schema",
                    "description": "An explanation about the purpose of this instance.",
                    "default": "",
                    "examples": [
                        "INSTITUTO SUPERIOR DE ENGENHARIA DE LISBOA"
                    ]
                },
                "acr": {
                    "$id": "#/properties/school/properties/acr",
                    "type": "string",
                    "title": "The acr schema",
                    "description": "An explanation about the purpose of this instance.",
                    "default": "",
                    "examples": [
                        "ISEL"
                    ]
                }
            }
        },
        "programme": {
            "$id": "#/properties/programme",
            "type": "object",
            "title": "The programme schema",
            "description": "An explanation about the purpose of this instance.",
            "default": {},
            "examples": [
                {
                    "name": "Licenciatura Engenharia Informática e Computadores"
                }
            ],
            "required": [
                "name"
            ],
            "additionalProperties": true,
            "properties": {
                "name": {
                    "$id": "#/properties/programme/properties/name",
                    "type": "string",
                    "title": "The name schema",
                    "description": "An explanation about the purpose of this instance.",
                    "default": "",
                    "examples": [
                        "Licenciatura Engenharia Informática e Computadores"
                    ]
                }
            }
        },
        "calendarSection": {
            "$id": "#/properties/calendarSection",
            "type": "string",
            "title": "The calendarSection schema",
            "description": "An explanation about the purpose of this instance.",
            "default": "",
            "examples": [
                "LI11D"
            ]
        },
        "courses": {
            "$id": "#/properties/courses",
            "type": "array",
            "title": "The courses schema",
            "description": "An explanation about the purpose of this instance.",
            "default": [],
            "examples": [
                [
                    {
                        "acr": "ALGA[I]",
                        "timetable": [
                            {
                                "type": "t",
                                "location": "E.1.31",
                                "beginTime": "14:00:00",
                                "endTime": "15:30:00",
                                "duration": "1:30:00",
                                "weekday": [
                                    "Monday"
                                ]
                            }
                        ]
                    },
                    {
                        "acr": "M2[I]",
                        "name": "Matemática II",
                        "timetable": [
                            {
                                "type": "t",
                                "location": "G.2.30",
                                "beginTime": "14:00:00",
                                "endTime": "15:30:00",
                                "duration": "1:30:00",
                                "weekday": [
                                    "Tuesday"
                                ]
                            },
                            {
                                "location": "G.2.30",
                                "beginTime": "14:00:00",
                                "endTime": "15:30:00",
                                "duration": "1:30:00",
                                "weekday": [
                                    "Friday"
                                ]
                            }
                        ]
                    }
                ]
            ],
            "additionalItems": true,
            "items": {
                "anyOf": [
                    {
                        "$id": "#/properties/courses/items/anyOf/0",
                        "type": "object",
                        "title": "The first anyOf schema",
                        "description": "An explanation about the purpose of this instance.",
                        "default": {},
                        "examples": [
                            {
                                "acr": "ALGA[I]",
                                "timetable": [
                                    {
                                        "type": "t",
                                        "location": "E.1.31",
                                        "beginTime": "14:00:00",
                                        "endTime": "15:30:00",
                                        "duration": "1:30:00",
                                        "weekday": [
                                            "Monday"
                                        ]
                                    }
                                ]
                            }
                        ],
                        "required": [
                            "acr",
                            "timetable"
                        ],
                        "additionalProperties": true,
                        "properties": {
                            "acr": {
                                "$id": "#/properties/courses/items/anyOf/0/properties/acr",
                                "type": "string",
                                "title": "The acr schema",
                                "description": "An explanation about the purpose of this instance.",
                                "default": "",
                                "examples": [
                                    "ALGA[I]"
                                ]
                            },
                            "timetable": {
                                "$id": "#/properties/courses/items/anyOf/0/properties/timetable",
                                "type": "array",
                                "title": "The timetable schema",
                                "description": "An explanation about the purpose of this instance.",
                                "default": [],
                                "examples": [
                                    [
                                        {
                                            "type": "t",
                                            "location": "E.1.31",
                                            "beginTime": "14:00:00",
                                            "endTime": "15:30:00",
                                            "duration": "1:30:00",
                                            "weekday": [
                                                "Monday"
                                            ]
                                        }
                                    ]
                                ],
                                "additionalItems": true,
                                "items": {
                                    "anyOf": [
                                        {
                                            "$id": "#/properties/courses/items/anyOf/0/properties/timetable/items/anyOf/0",
                                            "type": "object",
                                            "title": "The first anyOf schema",
                                            "description": "An explanation about the purpose of this instance.",
                                            "default": {},
                                            "examples": [
                                                {
                                                    "type": "t",
                                                    "location": "E.1.31",
                                                    "beginTime": "14:00:00",
                                                    "endTime": "15:30:00",
                                                    "duration": "1:30:00",
                                                    "weekday": [
                                                        "Monday"
                                                    ]
                                                }
                                            ],
                                            "required": [
                                                "type",
                                                "location",
                                                "beginTime",
                                                "endTime",
                                                "duration",
                                                "weekday"
                                            ],
                                            "additionalProperties": true,
                                            "properties": {
                                                "type": {
                                                    "$id": "#/properties/courses/items/anyOf/0/properties/timetable/items/anyOf/0/properties/type",
                                                    "type": "string",
                                                    "title": "The type schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "t"
                                                    ]
                                                },
                                                "location": {
                                                    "$id": "#/properties/courses/items/anyOf/0/properties/timetable/items/anyOf/0/properties/location",
                                                    "type": "string",
                                                    "title": "The location schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "E.1.31"
                                                    ]
                                                },
                                                "beginTime": {
                                                    "$id": "#/properties/courses/items/anyOf/0/properties/timetable/items/anyOf/0/properties/beginTime",
                                                    "type": "string",
                                                    "title": "The beginTime schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "14:00:00"
                                                    ]
                                                },
                                                "endTime": {
                                                    "$id": "#/properties/courses/items/anyOf/0/properties/timetable/items/anyOf/0/properties/endTime",
                                                    "type": "string",
                                                    "title": "The endTime schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "15:30:00"
                                                    ]
                                                },
                                                "duration": {
                                                    "$id": "#/properties/courses/items/anyOf/0/properties/timetable/items/anyOf/0/properties/duration",
                                                    "type": "string",
                                                    "title": "The duration schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "1:30:00"
                                                    ]
                                                },
                                                "weekday": {
                                                    "$id": "#/properties/courses/items/anyOf/0/properties/timetable/items/anyOf/0/properties/weekday",
                                                    "type": "array",
                                                    "title": "The weekday schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": [],
                                                    "examples": [
                                                        [
                                                            "Monday"
                                                        ]
                                                    ],
                                                    "additionalItems": true,
                                                    "items": {
                                                        "anyOf": [
                                                            {
                                                                "$id": "#/properties/courses/items/anyOf/0/properties/timetable/items/anyOf/0/properties/weekday/items/anyOf/0",
                                                                "type": "string",
                                                                "title": "The first anyOf schema",
                                                                "description": "An explanation about the purpose of this instance.",
                                                                "default": "",
                                                                "examples": [
                                                                    "Monday"
                                                                ]
                                                            }
                                                        ],
                                                        "$id": "#/properties/courses/items/anyOf/0/properties/timetable/items/anyOf/0/properties/weekday/items"
                                                    }
                                                }
                                            }
                                        }
                                    ],
                                    "$id": "#/properties/courses/items/anyOf/0/properties/timetable/items"
                                }
                            }
                        }
                    },
                    {
                        "$id": "#/properties/courses/items/anyOf/1",
                        "type": "object",
                        "title": "The second anyOf schema",
                        "description": "An explanation about the purpose of this instance.",
                        "default": {},
                        "examples": [
                            {
                                "acr": "M2[I]",
                                "name": "Matemática II",
                                "timetable": [
                                    {
                                        "type": "t",
                                        "location": "G.2.30",
                                        "beginTime": "14:00:00",
                                        "endTime": "15:30:00",
                                        "duration": "1:30:00",
                                        "weekday": [
                                            "Tuesday"
                                        ]
                                    },
                                    {
                                        "location": "G.2.30",
                                        "beginTime": "14:00:00",
                                        "endTime": "15:30:00",
                                        "duration": "1:30:00",
                                        "weekday": [
                                            "Friday"
                                        ]
                                    }
                                ]
                            }
                        ],
                        "required": [
                            "acr",
                            "name",
                            "timetable"
                        ],
                        "additionalProperties": true,
                        "properties": {
                            "acr": {
                                "$id": "#/properties/courses/items/anyOf/1/properties/acr",
                                "type": "string",
                                "title": "The acr schema",
                                "description": "An explanation about the purpose of this instance.",
                                "default": "",
                                "examples": [
                                    "M2[I]"
                                ]
                            },
                            "name": {
                                "$id": "#/properties/courses/items/anyOf/1/properties/name",
                                "type": "string",
                                "title": "The name schema",
                                "description": "An explanation about the purpose of this instance.",
                                "default": "",
                                "examples": [
                                    "Matemática II"
                                ]
                            },
                            "timetable": {
                                "$id": "#/properties/courses/items/anyOf/1/properties/timetable",
                                "type": "array",
                                "title": "The timetable schema",
                                "description": "An explanation about the purpose of this instance.",
                                "default": [],
                                "examples": [
                                    [
                                        {
                                            "type": "t",
                                            "location": "G.2.30",
                                            "beginTime": "14:00:00",
                                            "endTime": "15:30:00",
                                            "duration": "1:30:00",
                                            "weekday": [
                                                "Tuesday"
                                            ]
                                        },
                                        {
                                            "location": "G.2.30",
                                            "beginTime": "14:00:00",
                                            "endTime": "15:30:00",
                                            "duration": "1:30:00",
                                            "weekday": [
                                                "Friday"
                                            ]
                                        }
                                    ]
                                ],
                                "additionalItems": true,
                                "items": {
                                    "anyOf": [
                                        {
                                            "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/0",
                                            "type": "object",
                                            "title": "The first anyOf schema",
                                            "description": "An explanation about the purpose of this instance.",
                                            "default": {},
                                            "examples": [
                                                {
                                                    "type": "t",
                                                    "location": "G.2.30",
                                                    "beginTime": "14:00:00",
                                                    "endTime": "15:30:00",
                                                    "duration": "1:30:00",
                                                    "weekday": [
                                                        "Tuesday"
                                                    ]
                                                }
                                            ],
                                            "required": [
                                                "type",
                                                "location",
                                                "beginTime",
                                                "endTime",
                                                "duration",
                                                "weekday"
                                            ],
                                            "additionalProperties": true,
                                            "properties": {
                                                "type": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/0/properties/type",
                                                    "type": "string",
                                                    "title": "The type schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "t"
                                                    ]
                                                },
                                                "location": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/0/properties/location",
                                                    "type": "string",
                                                    "title": "The location schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "G.2.30"
                                                    ]
                                                },
                                                "beginTime": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/0/properties/beginTime",
                                                    "type": "string",
                                                    "title": "The beginTime schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "14:00:00"
                                                    ]
                                                },
                                                "endTime": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/0/properties/endTime",
                                                    "type": "string",
                                                    "title": "The endTime schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "15:30:00"
                                                    ]
                                                },
                                                "duration": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/0/properties/duration",
                                                    "type": "string",
                                                    "title": "The duration schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "1:30:00"
                                                    ]
                                                },
                                                "weekday": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/0/properties/weekday",
                                                    "type": "array",
                                                    "title": "The weekday schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": [],
                                                    "examples": [
                                                        [
                                                            "Tuesday"
                                                        ]
                                                    ],
                                                    "additionalItems": true,
                                                    "items": {
                                                        "anyOf": [
                                                            {
                                                                "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/0/properties/weekday/items/anyOf/0",
                                                                "type": "string",
                                                                "title": "The first anyOf schema",
                                                                "description": "An explanation about the purpose of this instance.",
                                                                "default": "",
                                                                "examples": [
                                                                    "Tuesday"
                                                                ]
                                                            }
                                                        ],
                                                        "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/0/properties/weekday/items"
                                                    }
                                                }
                                            }
                                        },
                                        {
                                            "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/1",
                                            "type": "object",
                                            "title": "The second anyOf schema",
                                            "description": "An explanation about the purpose of this instance.",
                                            "default": {},
                                            "examples": [
                                                {
                                                    "location": "G.2.30",
                                                    "beginTime": "14:00:00",
                                                    "endTime": "15:30:00",
                                                    "duration": "1:30:00",
                                                    "weekday": [
                                                        "Friday"
                                                    ]
                                                }
                                            ],
                                            "required": [
                                                "location",
                                                "beginTime",
                                                "endTime",
                                                "duration",
                                                "weekday"
                                            ],
                                            "additionalProperties": true,
                                            "properties": {
                                                "location": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/1/properties/location",
                                                    "type": "string",
                                                    "title": "The location schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "G.2.30"
                                                    ]
                                                },
                                                "beginTime": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/1/properties/beginTime",
                                                    "type": "string",
                                                    "title": "The beginTime schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "14:00:00"
                                                    ]
                                                },
                                                "endTime": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/1/properties/endTime",
                                                    "type": "string",
                                                    "title": "The endTime schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "15:30:00"
                                                    ]
                                                },
                                                "duration": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/1/properties/duration",
                                                    "type": "string",
                                                    "title": "The duration schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": "",
                                                    "examples": [
                                                        "1:30:00"
                                                    ]
                                                },
                                                "weekday": {
                                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/1/properties/weekday",
                                                    "type": "array",
                                                    "title": "The weekday schema",
                                                    "description": "An explanation about the purpose of this instance.",
                                                    "default": [],
                                                    "examples": [
                                                        [
                                                            "Friday"
                                                        ]
                                                    ],
                                                    "additionalItems": true,
                                                    "items": {
                                                        "anyOf": [
                                                            {
                                                                "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/1/properties/weekday/items/anyOf/0",
                                                                "type": "string",
                                                                "title": "The first anyOf schema",
                                                                "description": "An explanation about the purpose of this instance.",
                                                                "default": "",
                                                                "examples": [
                                                                    "Friday"
                                                                ]
                                                            }
                                                        ],
                                                        "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items/anyOf/1/properties/weekday/items"
                                                    }
                                                }
                                            }
                                        }
                                    ],
                                    "$id": "#/properties/courses/items/anyOf/1/properties/timetable/items"
                                }
                            }
                        }
                    }
                ],
                "$id": "#/properties/courses/items"
            }
        }
    }
}
```
