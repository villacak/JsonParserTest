package com.test.json.parser.utils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.Map;

/**
 * Created by klausvillaca on 2/4/17.
 *
 * Code copied from StackOverflow and added some improvements
 *
 */
public class JSONStreamParser implements Iterator<Map<String, Object>>, Closeable {
    private static final Logger LOG = LoggerFactory.getLogger(JSONStreamParser.class);

    private final InputStream inputStream;
    private JsonParser jsonParser;
    private boolean isInitialized;

    private Map<String, Object> nextObject;

    /**
     * Constructor passing the resource file name
     *
     * @param resourceFileName
     */
    public JSONStreamParser(final String resourceFileName) {
        this.inputStream = this.getClass().getClassLoader().getResourceAsStream(resourceFileName);
        this.isInitialized = false;
        this.nextObject = null;
    }

    /**
     * Start the json parser
     */
    private void init() {
        this.initJsonParser();
        this.initFirstElement();
        this.isInitialized = true;
    }


    /**
     * Init the json parser
     */
    private void initJsonParser() {
        final ObjectMapper objectMapper = new ObjectMapper();
        final JsonFactory jsonFactory = objectMapper.getFactory();

        try {
            this.jsonParser = jsonFactory.createParser(inputStream);
        } catch (final IOException e) {
            final StringBuilder msg = new StringBuilder("There was a problem setting up the JsonParser: ");
            LOG.error(msg.append("{}").toString(), e.getMessage());
            throw new RuntimeException(msg.append(e.getMessage()).toString(), e);
        }
    }

    /**
     * Init the first element
     */
    private void initFirstElement() {
        try {
            // Check that the first element is the start of an array
            final JsonToken arrayStartToken = this.jsonParser.nextToken();
            if (arrayStartToken != JsonToken.START_ARRAY) {
                final StringBuilder msg = new StringBuilder("The first element of the Json structure was expected to be a start array token, but it was: ")
                        .append(arrayStartToken);
                throw new IllegalStateException(msg.toString());
            }

            // Initialize the first object
            this.initNextObject();
        } catch (final Exception e) {
            final StringBuilder msg = new StringBuilder("There was a problem initializing the first element of the Json Structure: ");
            LOG.error(msg.append("{}").toString(),  e.getMessage());
            throw new RuntimeException(msg.append(e.getMessage()).toString(), e);
        }

    }

    /**
     * Init the next object
     */
    private void initNextObject() {
        try {
            final JsonToken nextToken = this.jsonParser.nextToken();

            // Check for the end of the array which will mean we're done
            if (nextToken == JsonToken.END_ARRAY) {
                this.nextObject = null;
                return;
            }

            // Make sure the next token is the start of an object
            if (nextToken != JsonToken.START_OBJECT) {
                final StringBuilder msg = new StringBuilder("The next token of Json structure was expected to be a start object token, but it was: ")
                        .append(nextToken);
                LOG.error(msg.toString());
                throw new IllegalStateException(msg.toString());
            }

            // Get the next product and make sure it's not null
            this.nextObject = this.jsonParser.readValueAs(new TypeReference<Map<String, Object>>() { });
            if (this.nextObject == null) {
                final String msg = "The next parsed object of the Json structure was null";
                LOG.error(msg);
                throw new IllegalStateException(msg);
            }
        } catch (final Exception e) {
            final String msg = "There was a problem initializing the next Object: ";
            LOG.error(msg + " {}", e.getMessage());
            throw new RuntimeException(msg + e.getMessage(), e);
        }
    }


    /**
     * Check if has next
     *
     * @return
     */
    public boolean hasNext() {
        if (!this.isInitialized) {
            this.init();
        }

        return this.nextObject != null;
    }


    /**
     * Get next as a map
     *
     * @return
     */
    public Map<String, Object> next() {
        // This method will return the current object and initialize the next object so hasNext will always have knowledge of the current state

        // Makes sure we're initialized first
        if (!this.isInitialized) {
            this.init();
        }

        // Store the current next object for return
        final Map<String, Object> currentNextObject = this.nextObject;

        // Initialize the next object
        this.initNextObject();
        return currentNextObject;
    }

    /**
     * Remove as empty as it's part of the interface,
     * leaving it empty as we don't need remove anything
     */
    public void remove() { }


    /**
     * Close file
     *
     * @throws IOException
     */
    public void close() throws IOException {
        IOUtils.closeQuietly(this.jsonParser);
        IOUtils.closeQuietly(this.inputStream);
    }


}
