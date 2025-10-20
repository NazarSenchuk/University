from pydantic_settings import BaseSettings

class Settings(BaseSettings):
    # Cloudflare R2 Configuration
    R2_ACCOUNT_ID: str
    R2_ACCESS_KEY_ID: str
    R2_SECRET_ACCESS_KEY: str
    R2_BUCKET_NAME: str
    R2_PUBLIC_URL: str  # e.g., "https://pub-<hash>.r2.dev"
    DATABASE_URL: str  # e.g., "postgresql://user:password@host:port/dbname"
    # File upload settings
    MAX_FILE_SIZE: int = 10 * 1024 * 1024  # 10MB
    ALLOWED_EXTENSIONS: set = {"jpg", "jpeg", "png", "gif", "webp"}
    
    class Config:
        env_file = ".env"

settings = Settings()