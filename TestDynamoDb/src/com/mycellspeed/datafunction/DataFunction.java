package com.mycellspeed.datafunction;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.s3.AmazonS3Client;

import com.amazonaws.AmazonServiceException;
import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.auth.PropertiesCredentials;
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClient;
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition;
import com.amazonaws.services.dynamodbv2.model.AttributeValue;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest;
import com.amazonaws.services.dynamodbv2.model.CreateTableResult;
import com.amazonaws.services.dynamodbv2.model.DeleteTableRequest;
import com.amazonaws.services.dynamodbv2.model.DeleteTableResult;
import com.amazonaws.services.dynamodbv2.model.DescribeTableRequest;
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement;
import com.amazonaws.services.dynamodbv2.model.KeyType;
import com.amazonaws.services.dynamodbv2.model.LocalSecondaryIndex;
import com.amazonaws.services.dynamodbv2.model.Projection;
import com.amazonaws.services.dynamodbv2.model.ProjectionType;
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput;
import com.amazonaws.services.dynamodbv2.model.PutItemRequest;
import com.amazonaws.services.dynamodbv2.model.ResourceNotFoundException;
import com.amazonaws.services.dynamodbv2.model.TableDescription;
import com.amazonaws.services.dynamodbv2.model.TableStatus;

public class DataFunction {

	public static final String ACCESS_KEY_ID = "AKIAJ3W2NBVOWKCXES7Q";
	public static final String SECRET_KEY = "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";
    
    static AmazonDynamoDBClient client;
    static SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
    static String productCatalogTableName = "ProductCatalog";
    static String forumTableName = "Forum";
    static String threadTableName = "Thread";
    static String replyTableName = "Reply";
    
    public void runUploadProcess() {
    	try {
			createClient();
			uploadMyTable("myTable");
			
			//uploadSampleProducts(productCatalogTableName);
			//deleteTable("myTable");			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    public void runUploadProcess01() {
    	try {
			createClient();
			uploadMyTable01("MusicCollection");
			
			//uploadSampleProducts(productCatalogTableName);
			//deleteTable("myTable");			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }    
    
    public void runDropTable() {
    	try {
			createClient();
			dropTable("myTable");	
			dropTable(productCatalogTableName);	
			dropTable(forumTableName);	
			dropTable(threadTableName);	
			dropTable(replyTableName);
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }    
    
    public void runCreateTable() {
    	try {
			createClient();

            // Parameter1: table name
            // Parameter2: reads per second
            // Parameter3: writes per second
            // Parameter4/5: hash key and type
            // Parameter6/7: range key and type (if applicable)
            
			createTable("myTable", 1L, 1L, "Id", "N");
            createTable(productCatalogTableName, 1L, 1L, "Id", "N");
            createTable(forumTableName, 1L, 1L, "Name", "S");
            createTable(threadTableName, 1L, 1L, "ForumName", "S", "Subject", "S" );
            createTable(replyTableName, 1L, 1L, "Id", "S", "ReplyDateTime", "S");		
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }      
    
    public void runCreateTable01() {
    	try {
			createClient();

            // Parameter1: table name
            // Parameter2: reads per second
            // Parameter3: writes per second
            // Parameter4/5: hash key and type
            // Parameter6/7: range key and type (if applicable)
            
			String tableName = "MusicCollection";

			CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);

			//ProvisionedThroughput
			createTableRequest.setProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long)5).withWriteCapacityUnits((long)5));

			//AttributeDefinitions
			ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("Artist").withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("SongTitle").withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("AlbumTitle").withAttributeType("S"));

			createTableRequest.setAttributeDefinitions(attributeDefinitions);
			        
			//KeySchema
			ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
			tableKeySchema.add(new KeySchemaElement().withAttributeName("Artist").withKeyType(KeyType.HASH));
			tableKeySchema.add(new KeySchemaElement().withAttributeName("SongTitle").withKeyType(KeyType.RANGE));

			createTableRequest.setKeySchema(tableKeySchema);
			        
			ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
			indexKeySchema.add(new KeySchemaElement().withAttributeName("Artist").withKeyType(KeyType.HASH));
			indexKeySchema.add(new KeySchemaElement().withAttributeName("AlbumTitle").withKeyType(KeyType.RANGE));

			Projection projection = new Projection().withProjectionType(ProjectionType.INCLUDE);
			ArrayList<String> nonKeyAttributes = new ArrayList<String>();
			nonKeyAttributes.add("Genre");
			nonKeyAttributes.add("Year");
			projection.setNonKeyAttributes(nonKeyAttributes);

			LocalSecondaryIndex localSecondaryIndex = new LocalSecondaryIndex()
			    .withIndexName("AlbumTitleIndex").withKeySchema(indexKeySchema).withProjection(projection);
			        
			ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
			localSecondaryIndexes.add(localSecondaryIndex);
			createTableRequest.setLocalSecondaryIndexes(localSecondaryIndexes);

			CreateTableResult result = client.createTable(createTableRequest);	
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }      
    
    public void runCreateTable02() {
    	try {
			createClient();

            // Parameter1: table name
            // Parameter2: reads per second
            // Parameter3: writes per second
            // Parameter4/5: hash key and type
            // Parameter6/7: range key and type (if applicable)
            
			String tableName = "CellSpeedData2013001";

			CreateTableRequest createTableRequest = new CreateTableRequest().withTableName(tableName);

			//ProvisionedThroughput
			createTableRequest.setProvisionedThroughput(new ProvisionedThroughput().withReadCapacityUnits((long)4).withWriteCapacityUnits((long)2));

			//AttributeDefinitions
			ArrayList<AttributeDefinition> attributeDefinitions= new ArrayList<AttributeDefinition>();
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("TS").withAttributeType("N"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("IMSI").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("IMEI").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("PhnNbr").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("NetOper").withAttributeType("S"));
			attributeDefinitions.add(new AttributeDefinition().withAttributeName("SimOper").withAttributeType("S"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("SimSrlNbr").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("SimState").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("SimCtryCode").withAttributeType("S"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("PhnType").withAttributeType("S"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("NetType").withAttributeType("S"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("MNC").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("MCC").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("GrpId").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("DvcLat").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("DvcLon").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("Mnfctr").withAttributeType("S"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("Brand").withAttributeType("S"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("Device").withAttributeType("S"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("Model").withAttributeType("S"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("OS").withAttributeType("S"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("DLSTR").withAttributeType("N"));
			//attributeDefinitions.add(new AttributeDefinition().withAttributeName("ULSTR").withAttributeType("N"));

			createTableRequest.setAttributeDefinitions(attributeDefinitions);
			        
			//KeySchema
			ArrayList<KeySchemaElement> tableKeySchema = new ArrayList<KeySchemaElement>();
			tableKeySchema.add(new KeySchemaElement().withAttributeName("TS").withKeyType(KeyType.HASH));
			tableKeySchema.add(new KeySchemaElement().withAttributeName("IMSI").withKeyType(KeyType.RANGE));

			createTableRequest.setKeySchema(tableKeySchema);
			        
			ArrayList<KeySchemaElement> indexKeySchema = new ArrayList<KeySchemaElement>();
			indexKeySchema.add(new KeySchemaElement().withAttributeName("TS").withKeyType(KeyType.HASH));
			indexKeySchema.add(new KeySchemaElement().withAttributeName("SimOper").withKeyType(KeyType.RANGE));

			Projection projection = new Projection().withProjectionType(ProjectionType.INCLUDE);
			ArrayList<String> nonKeyAttributes = new ArrayList<String>();
			nonKeyAttributes.add("SimOper");
			nonKeyAttributes.add("NetOper");
			projection.setNonKeyAttributes(nonKeyAttributes);

			LocalSecondaryIndex localSecondaryIndex = new LocalSecondaryIndex()
			    .withIndexName("SimOperIndex").withKeySchema(indexKeySchema).withProjection(projection);
			        
			ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
			localSecondaryIndexes.add(localSecondaryIndex);
			createTableRequest.setLocalSecondaryIndexes(localSecondaryIndexes);

			CreateTableResult result = client.createTable(createTableRequest);	
            
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }       
    
    private static void createClient() throws Exception {
        client = new AmazonDynamoDBClient(new BasicAWSCredentials(ACCESS_KEY_ID, SECRET_KEY));
        client.setEndpoint("dynamodb.us-west-2.amazonaws.com"); 
    }    
    
    public static void uploadMyTable(String tableName) {
        
        try {
            // Add books.
            Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
            
            item.put("Id", new AttributeValue().withN("101"));
            item.put("Title", new AttributeValue().withS("Book 101 Title"));
            item.put("ISBN", new AttributeValue().withS("111-1111111111"));
            item.put("Authors", new AttributeValue().withSS(Arrays.asList("Author1")));
            item.put("Price", new AttributeValue().withN("2"));
            item.put("Dimensions", new AttributeValue().withS("8.5 x 11.0 x 0.5"));
            item.put("PageCount", new AttributeValue().withN("500"));
            item.put("InPublication", new AttributeValue().withN("1"));
            item.put("ProductCategory", new AttributeValue().withS("Book"));
            
            PutItemRequest itemRequest = new PutItemRequest().withTableName(tableName).withItem(item);
            client.putItem(itemRequest);
            item.clear();
            
            item.put("Id", new AttributeValue().withN("102"));
            item.put("Title", new AttributeValue().withS("Book 102 Title"));
            item.put("ISBN", new AttributeValue().withS("222-2222222222"));
            item.put("Authors", new AttributeValue().withSS(Arrays.asList("Author1", "Author2")));
            item.put("Price", new AttributeValue().withN("20"));
            item.put("Dimensions", new AttributeValue().withS("8.5 x 11.0 x 0.8"));
            item.put("PageCount", new AttributeValue().withN("600"));
            item.put("InPublication", new AttributeValue().withN("1"));
            item.put("ProductCategory", new AttributeValue().withS("Book"));
            
            itemRequest = new PutItemRequest().withTableName(tableName).withItem(item);
            client.putItem(itemRequest);
            item.clear();   
                
        }   catch (AmazonServiceException ase) {
            System.err.println("Failed to create item in " + tableName + " " + ase);
        } 

    }    

    public static void uploadMyTable01(String tableName) {
        
        try {
            // Add books.
            Map<String, AttributeValue> item = new HashMap<String, AttributeValue>();
            
            item.put("Artist", new AttributeValue().withS("U2"));
            item.put("SongTitle", new AttributeValue().withS("Sunday Bloody Sunday"));
            item.put("AlbumTitle", new AttributeValue().withS("War"));
            item.put("Genre", new AttributeValue().withS("Rock/Pop"));
            item.put("Year", new AttributeValue().withN("1983"));
         
            PutItemRequest itemRequest = new PutItemRequest().withTableName(tableName).withItem(item);
            client.putItem(itemRequest);
            item.clear();
            
            item.put("Artist", new AttributeValue().withS("U2"));
            item.put("SongTitle", new AttributeValue().withS("Seconds"));
            item.put("AlbumTitle", new AttributeValue().withS("War"));
            item.put("Genre", new AttributeValue().withS("Rock/Pop"));
            item.put("Year", new AttributeValue().withN("1983"));
         
            itemRequest = new PutItemRequest().withTableName(tableName).withItem(item);
            client.putItem(itemRequest);
            item.clear();      
            
            item.put("Artist", new AttributeValue().withS("U2"));
            item.put("SongTitle", new AttributeValue().withS("Like A Song..."));
            item.put("AlbumTitle", new AttributeValue().withS("War"));
            item.put("Genre", new AttributeValue().withS("Rock/Pop"));
            item.put("Year", new AttributeValue().withN("1983"));
         
            itemRequest = new PutItemRequest().withTableName(tableName).withItem(item);
            client.putItem(itemRequest);
            item.clear();    
            
            item.put("Artist", new AttributeValue().withS("U2"));
            item.put("SongTitle", new AttributeValue().withS("Drowning Man"));
            item.put("AlbumTitle", new AttributeValue().withS("War"));
            item.put("Genre", new AttributeValue().withS("Rock/Pop"));
            item.put("Year", new AttributeValue().withN("1983"));
         
            itemRequest = new PutItemRequest().withTableName(tableName).withItem(item);
            client.putItem(itemRequest);
            item.clear();             
            
        }   catch (AmazonServiceException ase) {
            System.err.println("Failed to create item in " + tableName + " " + ase);
        } 

    }  
    
    @SuppressWarnings("unused")
	private static void dropTable(String tableName){
        try {
            
            DeleteTableRequest request = new DeleteTableRequest()
                .withTableName(tableName);
            
            DeleteTableResult result = client.deleteTable(request);
               
        } catch (AmazonServiceException ase) {
            System.err.println("Failed to delete table " + tableName + " " + ase);
        }

    }
    
    private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
            String hashKeyName, String hashKeyType) {
        
        createTable(tableName, readCapacityUnits, writeCapacityUnits, hashKeyName,  hashKeyType, null, null);    
    }
    
    private static void createTable(String tableName, long readCapacityUnits, long writeCapacityUnits,
            String hashKeyName, String hashKeyType, String rangeKeyName, String rangeKeyType) {
        
        try {
            System.out.println("Creating table " + tableName);
    		ArrayList<KeySchemaElement> ks = new ArrayList<KeySchemaElement>();
    		ArrayList<AttributeDefinition> attributeDefinitions = new ArrayList<AttributeDefinition>();

    		ks.add(new KeySchemaElement().withAttributeName(
    				hashKeyName).withKeyType(KeyType.HASH));
       		attributeDefinitions.add(new AttributeDefinition().withAttributeName(
       				hashKeyName).withAttributeType(hashKeyType));
 
            if (rangeKeyName != null){
           		ks.add(new KeySchemaElement().withAttributeName(
        				rangeKeyName).withKeyType(KeyType.RANGE));
        		attributeDefinitions.add(new AttributeDefinition().withAttributeName(
        				rangeKeyName).withAttributeType(rangeKeyType));
            }
     		        	            
            // Provide initial provisioned throughput values as Java long data types
            ProvisionedThroughput provisionedthroughput = new ProvisionedThroughput()
                .withReadCapacityUnits(readCapacityUnits)
                .withWriteCapacityUnits(writeCapacityUnits);
            
            CreateTableRequest request = new CreateTableRequest()
                .withTableName(tableName)
                .withKeySchema(ks)
                .withProvisionedThroughput(provisionedthroughput);
            
            // If this is the Reply table, define a local secondary index
            if (replyTableName.equals(tableName)) {
        		attributeDefinitions.add(new AttributeDefinition().withAttributeName("PostedBy").withAttributeType("S"));

        		ArrayList<KeySchemaElement> iks = new ArrayList<KeySchemaElement>();
        		iks.add(new KeySchemaElement().withAttributeName(
        				hashKeyName).withKeyType(KeyType.HASH));
        		iks.add(new KeySchemaElement().withAttributeName(
        				"PostedBy").withKeyType(KeyType.RANGE));

        		LocalSecondaryIndex lsi = new LocalSecondaryIndex().withIndexName("PostedByIndex")
                		.withKeySchema(iks)
                		.withProjection(new Projection().withProjectionType(ProjectionType.KEYS_ONLY));
                
        		ArrayList<LocalSecondaryIndex> localSecondaryIndexes = new ArrayList<LocalSecondaryIndex>();
        		localSecondaryIndexes.add(lsi);
        		
        		request.setLocalSecondaryIndexes(localSecondaryIndexes);
            }
            
            request.setAttributeDefinitions(attributeDefinitions);

            CreateTableResult result = client.createTable(request);
            
        } catch (AmazonServiceException ase) {
            System.err.println("Failed to create table " + tableName + " " + ase);
        }
    }    

    
}


