#include <iostream>
#include <string>

class participan {
public:
	void increasePoints(int i) {
		point += i;
	};
	void setName(std::string n ) {
		name = n;
	}
	std::string getName() {
		return name;
	}
	int getPoints() {
		return point;
	}
private:
	std::string name;
	int point;
};

class olimpiada_task {
public:
	olimpiada_task() {
		std::cin >> *this;
		
	};
	friend std::istream& operator>>(std::istream& is, olimpiada_task& obj);
	
	participan* getParticipant() { return participant; }
	int getTasks() { return tasks; };
	int getCompletedTasks() { return completed_tasks; };


	void setParticipant(participan* p) { participant = p; };
	void setTasks(int i) { tasks = i; };
	void setCompletedTasks(int i ) { completed_tasks = i; };
	

private:


	participan* participant;
	int tasks;
	int completed_tasks;
};

std::istream& operator>>(std::istream& is, olimpiada_task& obj) {
	std::cout << "Введіть кількість завдань:";
	int count;
	while (!(is >> count) ) {
		std::cout << "Помилка! Введіть коректну кількість завдань: ";
		std::cin.clear();
		std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
	};
	std::cout << "Введіть кількість правильно зроблених завдань:";
	int count_completed;
	while (!(is >> count_completed) || count_completed > count) {
		std::cout << "Помилка! Введіть коректну кількість зроблених завдань: ";
		std::cin.clear();
		std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
	};
	
	obj.setTasks(count);
	obj.setCompletedTasks(count_completed);
	return is;
}


class test_all_or_nothing : public olimpiada_task
{
public:
	test_all_or_nothing(participan* p) {
		
		this->setParticipant(p);
		std::cout << "Введіть кількість балів за це завдання:";
		
		while (!(std::cin >> max_point) ) {
			std::cout << "Помилка! Введіть коректну кількість балів: ";
			std::cin.clear();
			std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
		};
		std::cout << '\n';
		if (this->getCompletedTasks() == this->getTasks()) {
			this->getParticipant()->increasePoints(max_point);
		}

		 

	};
private:

	int max_point;

};


void init_participants(participan* participants[100], int count) {
	
	for (int i = 0; i < count; i++) {
		std::cout << "Введіть назву учасника: ";
		participants[i] =  new participan();
		std::string name;
		while (!(std::cin >> name)) {
			std::cout << "Помилка! Введіть коректну назву: ";
			std::cin.clear();
			std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
		
		};
		participants[i]->setName(name);
		
	
	
	}

	std::cout << "Список учнів: \n";
	for (int i = 0; i < count; i++) {
		std::cout << participants[i]->getName()  << '\n';
	
	}



}

participan* searchParticipant(participan* participants[100], int count, std::string name) {
	for (int i = 0; i < count; i++) {
		if (participants[i]->getName() == name)
			return participants[i];
	}
	return nullptr;
	

}

void sorting(int count, participan* participants[100]) {
	for (int i = 0; i < count; i++) {
		int smallest = i;
		for (int b = (i + 1); b < count; b++) {
			if (participants[b]->getPoints() > participants[smallest]->getPoints()) {
				smallest = b;
			}



		}
		participan* temp;
		temp = participants[i];
		participants[i] = participants[smallest];
		participants[smallest] = temp;




	}


}

void init_tests(test_all_or_nothing* tests[100], int count , int count_participants ,participan* participants[100]) {
	for (int i = 0; i < count; i++) {
		std::string participant;
		participan* participant_founded = nullptr;

		std::cout << "Назва учня який проходив тест:";
		while (!(std::cin >> participant) || (participant_founded = searchParticipant(participants , count_participants , participant))== nullptr) {
			std::cout << "Помилка! Введіть коректну назву: ";
			std::cin.clear();
			std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');
		
		};




		tests[i] = new test_all_or_nothing(participant_founded) ; 
	

	}

		sorting(count_participants, participants);

	std::cout << "\nСтатистика учнів:\n";
	for (int i = 0; i < count_participants; i++) {
		std::cout << participants[i]->getName() << " Бали:" << participants[i]->getPoints() << '\n';
	
	
	}


}


void main() {
	participan* participants[100];
	test_all_or_nothing* tests[100];
	int count_tests, count_participants;
	std::cout << "Введіть кількість учнів які проходили тест:";
	while (!(std::cin >> count_participants)) {
		std::cout << "Помилка! Введіть коректну кількість: ";
		std::cin.clear();
		std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');

	};

	init_participants(participants , count_participants);
	std::cout << "Введіть кількість тестів:";
	while (!(std::cin >> count_tests)) {
		std::cout << "Помилка! Введіть коректну кількість: ";
		std::cin.clear();
		std::cin.ignore(std::numeric_limits<std::streamsize>::max(), '\n');

	};


	init_tests(tests, count_tests, count_participants, participants);

	



}


