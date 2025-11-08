#include "crow.h"
#include "ImageController.h"
#include "DatabaseManager.h"
#include "config/Config.h"
#include <iostream>

int main() {
    crow::SimpleApp app;
    
    DatabaseConfig db_config;

    ServerConfig server_config;
    server_config.port = 8080;

    R2Config r2_config;
    r2_config.endpoint = "https://086cf4cab6f623952bd0831a164dc89b.r2.cloudflarestorage.com";
    r2_config.access_key = "04d7b1d006c66373a5f989eacb72e58e";
    r2_config.secret_key = "0d16a302e2262503ffd774240e2875a86de91b2fc70ffdaf55f5784bac97200d";
    r2_config.bucket_name = "images";
    
    // Ініціалізація бази даних
    DatabaseManager db_manager(db_config);
    if (!db_manager.connect()) {
        std::cerr << "Failed to connect to database!" << std::endl;
        return 1;
    }

    R2Manager r2_manager(r2_config);
    if(!r2_manager.testConnect()){

        std::cerr << "Помилка з  конектом до R2" << std::endl;
        return 1;
    }
    
    // Ініціалізація контролера
    ImageController image_controller(db_manager , r2_manager);
    
    // Реєстрація роутів
    image_controller.registerRoutes(app);
    
    // Додаткові ендпоїнти
    CROW_ROUTE(app, "/api/images/status/<string>")
        .methods("GET"_method)
        ([&image_controller](const crow::request& req, const std::string& status) {
            return image_controller.getImagesByStatus(req , status);
        });
    
    // CORS middleware
    CROW_CATCHALL_ROUTE(app)
    ([](const crow::request& req, crow::response& res) {
        res.add_header("Access-Control-Allow-Origin", "*");
        res.add_header("Access-Control-Allow-Headers", "Content-Type");
        res.add_header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS");
        
        if (req.method == "OPTIONS"_method) {
            res.end();
        } else {
            res.code = 404;
            res.end("Not Found");
        }
    });
    
    std::cout << "C++ API Server with PostgreSQL starting on http://localhost:" 
              << server_config.port << std::endl;
    std::cout << "Available endpoints:" << std::endl;
    std::cout << "  POST   /api/images" << std::endl;
    std::cout << "  GET    /api/images" << std::endl;
    std::cout << "  GET    /api/images/<id>" << std::endl;
    std::cout << "  GET    /api/images/status/<status>" << std::endl;
    std::cout << "  GET    /api/stats" << std::endl;
    
    app.port(server_config.port).multithreaded().run();
    
    return 0;
}