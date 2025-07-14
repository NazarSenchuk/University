#include <iostream>
#include <string>
#include <ctime>

class command {
public:

	friend std::istream& operator>>(std::istream& is, command& obj);

	int getPoints() { return points; };
	int setPoints(int point) { points = point;  };
	void increasePoints(int number) { points += number; };
	std::string getName() { return name; };
	command(std::string command_name ) {
		name = command_name;
	
	}
	command() {
		std::cin >> *this;
	}
private:
	int points = 0;
	std::string name;





};
class competition {
public:
	competition() { std::cin >> *this; };

	std::string getName() { return name; }
	std::tm getStart() { return startDate; }
	std::tm getEnd() { return endDate; }
	friend std::istream& operator>>(std::istream&, competition&);



protected:
	std::string name;
	std::tm startDate;
	std::tm endDate;



private:
	int validName(std::string name) {


	}
	int validDate(std::tm startDate, std::tm endDate) {
		std::string error = "\n Помилка: Дата кінця має бути пізніше  ніж дата початку!! \n";
		if (startDate.tm_year < endDate.tm_year) {
			return 1;
		}
		else if (startDate.tm_year == endDate.tm_year) {
			if (startDate.tm_mon < endDate.tm_mon) {
				return 1;
			}
			else if (startDate.tm_mon == endDate.tm_mon) {
				if (startDate.tm_mday < endDate.tm_mday) {
					return 1;
				}
				else if (startDate.tm_mday == endDate.tm_mday) {


				}
				else
					std::cout << error;
					return 0;

			}
			else
				std::cout << error;
				return 0;


		}
		else
			std::cout << error;
			return 0;


	}

	std::tm  inputDate(std::istream& is) {
		std::tm time = { 0 };

		std::cout << "Рік:";
		int year;
		while (!(is >> year) || year < 1900) {
			std::cout << "Помилка! Введіть коректний рік: ";
			std::cin.clear();      
			std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
		};
		time.tm_year = year;
		
		
		
		std::cout << "Місяць:";
		int mon;
		while (!(is >> mon) || mon <0 || mon > 11) {
			std::cout << "Помилка! Введіть коректний місяць: ";
			std::cin.clear();
			std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
		};
		time.tm_mon = mon;




		std::cout << "День:";
		int day;
		while (!(is >> day) || day < 1 || day > 31) {
			std::cout << "Помилка! Введіть коректний день: ";
			std::cin.clear();
			std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
		};
		time.tm_mday = day;

		return time;
	
	}




};
std::istream& operator>>(std::istream& is , competition& obj) {
	
	std::cout << "Введіть назву змагання:";
	is >> obj.name;

	

	std::tm start_time = { 0 };
	std::cout << "Введіть час початку змагання:\n";
	start_time = obj.inputDate(is);
	
    std::cout << "Введіть час кінця змагання:\n";
	std::tm end_time = { 0 };
	do {
		end_time = obj.inputDate(is);
	} while (obj.validDate(start_time, end_time) == 0);

	obj.startDate = start_time;
	obj.endDate = end_time;
	return is;




}
std::istream& operator>>(std::istream& is, command& obj) {
	std::cout << "Назва команди:";

	is >> obj.name;
	return is;
}



class cup : public competition {
public:
	command getFinalist() { return *finalist; }
	command getWinner() { return *winner; }
	cup(command* final, command* win) { finalist = final; winner = win; finalist->increasePoints(2); winner->increasePoints(3); };
	cup() {};
	void setFinalist(command* final ) { finalist = final; finalist->increasePoints(2); }

	void setWinner(command* win) { winner = win; winner->increasePoints(3); }
protected:
	command* finalist;
	command* winner;
	
private:





};


command* searchCommand(int count , command* commands[10], std::string command) {
	for (int i = 0; i != count; i++) {
		if (commands[i]->getName() == command) {
			return commands[i];
		}
	
	}
	return nullptr;

};

void commands_initializing(command* commands[10], int* count) {
	std::cout << "Введіть кількість команд:";
	while (!(std::cin >> *count) || *count < 0) {
		std::cout << "Введіть коректну кількість команд:";
		std::cin.clear();
		std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');

	}
	
	
	for (int i = 0; i < *count; i++) {
		commands[i] = new command();

	}
	std::cout << "Список команд:\n";
	for (int i = 0; i < *count; i++) {
		std::cout << commands[i]->getName() << '\n';

	}
}

void cups_initialization(command* commands[10] , cup* cups[10]  , int* count , int* cups_count ) {
	std::cout << "Введення інформації про змагання:\n";
	std::cout << "Кількість змагань:";
	
	while (!(std::cin >> *cups_count) || *cups_count < 0) {
		std::cout << "Введіть коректну кількість змагань:";
		std::cin.clear();
		std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');

	}
	
	for (int i = 0; i < *cups_count; i++) {
		std::cout << i + 1 << ','<<'\n';
		cups[i] = new cup();
		command* finalist_searched = nullptr;
		do {
			std::string finalist;
			std::cout << "Фіналіст змагання:";
			std::cin >> finalist;

			finalist_searched = searchCommand(*count, commands, finalist);
			if (finalist_searched == nullptr) {
				std::cout << "Фіналіст не знайдений, спробуйте ще раз.\n";
			}
		} while (finalist_searched == nullptr);

		command* winner_searched = nullptr;
		do {
			std::string winner;
			std::cout << "Переможець  змагання:";
			std::cin >> winner;

			winner_searched = searchCommand(*count, commands, winner);
			if (winner_searched == nullptr) {
				std::cout << "Переможець не знайдений, спробуйте ще раз.\n";
			}
		} while (winner_searched == nullptr);

		cups[i]->setFinalist(finalist_searched);
		cups[i]->setWinner(winner_searched);

	}

}


void printStatistic(int count , int cups_count , command* commands[10] , cup* cups[10]) {
	std::cout << "\n Статистика команд:\n";

	

	for (int i = 0;i < count; i++) {
		int wins = 0;
		int finals =0 ;
		for (int b = 0; b < cups_count; b++) {
			if (cups[b]->getFinalist().getName() == commands[i]->getName()) {
				finals++;
			}
			if (cups[b]->getWinner().getName() == commands[i]->getName()) {
				wins++;
			}
		}

		std::cout << "Назва:" << commands[i]->getName()<< " Очків:" << commands[i]->getPoints() << " Перемог:" << wins << " Фіналів:" << finals << '\n';
	
	}


}

int main(){
	int count;
	int cups_count;
	command* commands[10];
	cup* cups[10];
	commands_initializing(commands , &count);

	std::cout << "\n\n";

	cups_initialization(commands, cups, &count , &cups_count);
	
	printStatistic(count , cups_count , commands , cups);
}

10 варіант