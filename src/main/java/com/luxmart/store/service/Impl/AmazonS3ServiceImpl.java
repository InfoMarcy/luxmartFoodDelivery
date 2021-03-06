package com.luxmart.store.service.Impl;

import java.io.InputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.GetObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.amazonaws.services.s3.model.S3Object;
import com.luxmart.store.service.AmazonS3Services;

@Service("amazonS3")
public class AmazonS3ServiceImpl implements AmazonS3Services {
	
	private Logger logger = LoggerFactory.getLogger(AmazonS3ServiceImpl.class);
	
	@Autowired
	private AmazonS3 s3client;
 
	@Value("${cloud.aws.bucket}")
	private String bucketName;

	
	
	// upload image to amazon S3
	@Override
	public String uploadFile(String keyName, InputStream productImage) {
		
try {
			
	          
			// saves  on s3 with public read access
		   s3client.putObject(new PutObjectRequest(bucketName, keyName, productImage, 
				              new ObjectMetadata()).withCannedAcl(CannedAccessControlList.PublicRead));
		   
		   //get a reference to the image object
		   S3Object s3Object = s3client.getObject(new GetObjectRequest(bucketName, keyName));
		   
		   // get the newly created image url
		   String imageUrl = s3Object.getObjectContent().getHttpRequest().getURI().toString();
		   
		   
		   // return the link of the image
		  return imageUrl;
		  
	      //  logger.info("===================== Upload File - Done! =====================");
	        
		} catch (AmazonServiceException ase) {
			logger.info("Caught an AmazonServiceException from PUT requests, rejected reasons:");
			logger.info("Error Message:    " + ase.getMessage());
			logger.info("HTTP Status Code: " + ase.getStatusCode());
			logger.info("AWS Error Code:   " + ase.getErrorCode());
			logger.info("Error Type:       " + ase.getErrorType());
			logger.info("Request ID:       " + ase.getRequestId());
        } catch (AmazonClientException ace) {
            logger.info("Caught an AmazonClientException: ");
            logger.info("Error Message: " + ace.getMessage());
        }


           return null; 
	}



	@Override
	public void deleteImage(String keyName) {
			s3client.deleteObject(new DeleteObjectRequest(bucketName, keyName));
	}
	


	
 
}
