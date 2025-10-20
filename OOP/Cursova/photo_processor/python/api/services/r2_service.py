import boto3
from botocore.exceptions import ClientError
from fastapi import UploadFile, HTTPException
import uuid
from typing import Optional
import os

from config import settings

class R2Service:
    def __init__(self):
        self.s3_client = boto3.client(
            's3',
            endpoint_url=f'https://{settings.R2_ACCOUNT_ID}.r2.cloudflarestorage.com',
            aws_access_key_id=settings.R2_ACCESS_KEY_ID,
            aws_secret_access_key=settings.R2_SECRET_ACCESS_KEY,
            region_name='auto'
        )
        self.bucket_name = settings.R2_BUCKET_NAME
        self.public_url = settings.R2_PUBLIC_URL

    def generate_filename(self, original_filename: str) -> str:
        """Generate unique filename with original extension"""
        ext = original_filename.split('.')[-1]
        unique_id = uuid.uuid4().hex
        return f"{unique_id}.{ext}"

    async def upload_file(self, file: UploadFile) -> dict:
        """Upload file to R2 and return file info"""
        # Validate file size
        contents = await file.read()
        if len(contents) > settings.MAX_FILE_SIZE:
            raise HTTPException(status_code=400, detail="File too large")
        
        # Validate file extension
        file_extension = file.filename.split('.')[-1].lower()
        if file_extension not in settings.ALLOWED_EXTENSIONS:
            raise HTTPException(status_code=400, detail="File type not allowed")
        
        # Generate unique filename
        filename = self.generate_filename(file.filename)
        
        try:
            # Upload to R2
            self.s3_client.put_object(
                Bucket=self.bucket_name,
                Key=filename,
                Body=contents,
                ContentType=file.content_type
            )
            
            # Generate public URL
            file_url = f"{self.public_url}/{filename}"
            
            return {
                "filename": filename,
                "original_filename": file.filename,
                "url": file_url,
                "content_type": file.content_type,
                "size": len(contents)
            }
            
        except ClientError as e:
            raise HTTPException(status_code=500, detail=f"Upload failed: {str(e)}")

    async def delete_file(self, filename: str) -> bool:
        """Delete file from R2"""
        try:
            self.s3_client.delete_object(Bucket=self.bucket_name, Key=filename)
            return True
        except ClientError:
            return False

    def get_file_url(self, filename: str) -> str:
        """Generate public URL for filename"""
        return f"{self.public_url}/{filename}"

# Singleton instance
r2_service = R2Service()