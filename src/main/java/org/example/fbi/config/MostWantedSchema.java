package org.example.fbi.config;
import org.apache.parquet.schema.MessageType;
import org.apache.parquet.schema.MessageTypeParser;

public class MostWantedSchema {

        private static final String SCHEMA_STRING =
                "message Crime {" +
                        "  required binary UID (UTF8);" +
                        "  optional binary Description (UTF8);" +
                        "  optional binary Subject (UTF8);" +
                        "  optional binary Status (UTF8);" +
                        "  optional binary Reward_Text (UTF8);" +
                        "  optional binary Publication (UTF8);" +
                        "  optional binary Sex (UTF8);" +
                        "  optional binary Race (UTF8);" +
                        "  optional binary Hair (UTF8);" +
                        "  optional binary Age_Range (UTF8);" +
                        "  optional binary Weight (UTF8);" +
                        "  optional binary Place_of_Birth (UTF8);" +
                        "  optional binary Warning_Message (UTF8);" +
                        "  optional binary Complexion (UTF8);" +
                        "  optional binary Nationality (UTF8);" +
                        "  optional binary Aliases (UTF8);" +
                        "  optional binary Image_Original (UTF8);" +
                        "}";

        public static MessageType getSchema() {
            return MessageTypeParser.parseMessageType(SCHEMA_STRING);
        }
}


