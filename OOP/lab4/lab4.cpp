#include <iostream>
#include <string>
#include <ctime>
#include <vector>
#include <map>

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
public:
	virtual void setFinalist(command* final) = 0; 
	virtual void setWinner(command* win) = 0;
	virtual command getFinalist() = 0;
	virtual command getWinner() = 0;
	virtual void printInfo() const {
		std::cout << "Змагання: " << name << std::endl;
		std::cout << "Дата початку: " << startDate.tm_year << '.' << startDate.tm_mon << '.' << startDate.tm_mday;
		std::cout << "Дата кінця: " << endDate.tm_year << '.' << endDate.tm_mon << '.' << endDate.tm_mday;


	}


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
	void printInfo() const override {
		competition::printInfo();
		std::cout << "Тип: Cup" << std::endl;
		std::cout << "Переможець: " << winner << std::endl;
		std::cout << "Фіналіст: " << finalist << std::endl;
		std::cout << "Очків отримано: " << winner << " (3), " << finalist << " (2)" << std::endl;
	}
	void setWinner(command* win) { winner = win; winner->increasePoints(3); }
protected:
	command* finalist;
	command* winner;
	
private:





};

class supercup : public competition {
public:
	command getFinalist() { return *finalist; }
	command getWinner() { return *winner; }
	supercup(command* final, command* win) { finalist = final; winner = win; finalist->increasePoints(1); winner->increasePoints(2); };
	supercup() {};
	void setFinalist(command* final) { finalist = final; finalist->increasePoints(1); }
	void printInfo() const override {
		competition::printInfo();
		std::cout << "Тип: SuperCup" << std::endl;
		std::cout << "Переможець: " << winner << std::endl;
		std::cout << "Фіналіст: " << finalist << std::endl;
		std::cout << "Очків отримано: " << winner << " (3), " << finalist << " (2)" << std::endl;
	}
	void setWinner(command* win) { winner = win; winner->increasePoints(2); }
protected:
	command* finalist;
	command* winner;

private:





};


command* searchCommand(int count , std::vector<command*>& commands, std::string command) {
	for (int i = 0; i != count; i++) {
		if (commands[i]->getName() == command) {
			return commands[i];
		}
	
	}
	return nullptr;

};

void commands_initializing(std::vector<command*>& pCommands , int *count) {
	
	std::cout << "Введіть кількість команд:";
	while (!(std::cin >> *count) || *count < 0) {
		std::cout << "Введіть коректну кількість команд:";
		std::cin.clear();
		std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');

	}
	pCommands.resize(*count);
	
	for (int i = 0; i < *count; i++) {
		pCommands[i] = new command();

	}
	std::cout << "Список команд:\n";
	for (int i = 0; i < *count; i++) {
		std::cout << pCommands[i]->getName() << '\n';

	}
}

void cups_initialization(std::vector<command*>& pCommands, std::vector<competition*>& pCompetitions, int* count , int* cups_count, std::map<std::string, std::vector<int>>& statistic) {
	std::cout << "Введення інформації про змагання:\n";
	std::cout << "Кількість змагань:";
	
	while (!(std::cin >> *cups_count) || *cups_count < 0) {
		std::cout << "Введіть коректну кількість змагань:";
		std::cin.clear();
		std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');

	}

	pCompetitions.resize(*cups_count);
	
	for (int i = 0; i < *cups_count; i++) {
		std::cout << i + 1 << ','<<'\n';
		std::string type;
		std::cout << "Введіть тип змагання(Кубок чи Суперкубок):";
		std::cin >> type ;
		if (type == "Кубок")
			pCompetitions[i] = new cup();
		else
			pCompetitions[i] = new supercup();
			
		command* finalist_searched = nullptr;
		do {
			std::string finalist;
			std::cout << "Фіналіст змагання:";
			std::cin >> finalist;

			finalist_searched = searchCommand(*count, pCommands, finalist);
			if (finalist_searched == nullptr) {
				std::cout << "Фіналіст не знайдений, спробуйте ще раз.\n";
			}
		} while (finalist_searched == nullptr);

		command* winner_searched = nullptr;
		do {
			std::string winner;
			std::cout << "Переможець  змагання:";
			std::cin >> winner;

			winner_searched = searchCommand(*count, pCommands, winner);
			if (winner_searched == nullptr) {
				std::cout << "Переможець не знайдений, спробуйте ще раз.\n";
			}
		} while (winner_searched == nullptr);

		pCompetitions[i]->setFinalist(finalist_searched);
		statistic[finalist_searched->getName()][0] += 1;
		statistic[finalist_searched->getName()][2] = finalist_searched->getPoints();
		pCompetitions[i]->setWinner(winner_searched);
		statistic[winner_searched->getName()][1] += 1;
		statistic[winner_searched->getName()][2] = finalist_searched->getPoints();
	}

}


void printStatistic(int count , int cups_count , std::vector<command*>& commands, std::vector<competition*>& cups, std::map<std::string, std::vector<int>>& statistic) {
	std::cout << "\n Статистика команд:\n";

	

	for (int i = 0;i < count; i++) {
		

		std::cout << "Назва:" << commands[i]->getName()<< " Очків:" << commands[i]->getPoints() << " Перемог:" << statistic[commands[i]->getName()][1]<< " Фіналів:" << statistic[commands[i]->getName()][0] << '\n';
	
	}

	

}

void init_statistic(std::vector<command*>& commands, int count ,  std::map<std::string, std::vector<int>>& statistic){

	for (int i = 0; i < count; i++) {
		statistic[commands[i]->getName()] = {0,0,0};
		
	
	}
}

int main(){
	std::map<std::string, std::vector<int>> statistic;
	std::vector<competition*> pCompetitions;
	std::vector<command*> pCommands;

	int count;
	int competition_count;
	
	commands_initializing(pCommands ,  &count);
	init_statistic(pCommands, count , statistic);
	std::cout << "\n\n";

	cups_initialization(pCommands, pCompetitions, &count , &competition_count , statistic);
	
	printStatistic(count , competition_count , pCommands, pCompetitions,statistic);
}
