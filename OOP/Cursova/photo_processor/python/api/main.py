from fastapi import FastAPI
from routes import location, person, photo
from database import engine, Base
app = FastAPI(title="Photo Management API", version="1.0.0")
print("Creating database tables...")
Base.metadata.create_all(bind=engine)
print("Tables created successfully!")
# Include routers
app.include_router(location.router)
app.include_router(person.router)
app.include_router(photo.router)

@app.get("/")
def read_root():
    return {"message": "Photo Management API", "version": "1.0.0"}

@app.get("/health")
def health_check():
    return {"status": "healthy"}

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="0.0.0.0", port=8000) 