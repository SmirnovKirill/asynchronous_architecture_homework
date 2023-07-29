1. Сервис авторизации
   * **Про какие сервисы знает**: ни про какие.
   * **Какие ивенты кидает**: никакие.
   * **Таблицы**:
      1. *user* - user_id, role, creation_time.
   * **Задачи**:
     * Ручка получения токена по фотке попугая, постом в теле прямо фотка. Сервис по своей логике верифицирует что клюв виден, проверяет размер.
     * Ручка получения юзера по айди.
     * Ручка получения рандомного юзера среди юзеров с переданными ролями.
   
2. Сервис-трекер
   * **Про какие сервисы знает**: про сервис авторизации.
   * **Какие ивенты кидает**:
     1. *task_create* - таска создалась.
     2. *task_assign* - таска заассайнилась на юзера.
     3. *task_resolve* - таска решена.
     4. *tasks_reassign* - таска, а не ивент, предназначена самому этому сервису для выполнения в фоне.
   * **Таблицы**:
     1. *task* - task_id, title, description, status (IN_PROGRESS/DONE), assignee_user_id, creation_time.
   * **Задачи**:
     * Страница - на ней выводить список своих задач (и кнопка отметить выполненным) + сделать сразу форму создания новой таски + у менеджеров и админов кнопка заассайнить.
     * Ручка - отдать таски по айдишкам (нужно для сервиса аккаунтинга), чтение из *task*.
     * Ручка - список своих тасок, чтение из *task*.
     * Ручка - создай таску. Вызов метода отдачи рандомного юзера, создание таски, отправка ивентов *task_create* и *task_assign*.
     * Ручка - отметить выполненной. Валидация что задача своя. Отправка ивента *task_resolve*.
     * Ручка - "заассайнить". Отправка сервисом самому себе *tasks_reassign*.
     * Метод - назначь таске рандомного юзера. Там вызов ручки выбора рандомного юзера сервиса авторизации, само назначение и отправка ивента *task_assign*.
     * Листенер *tasks_reassign*. Батчами проходим все таски, вызываем метод "назначь таске рандомного юзера". В базе "проход" таски из трекера нигде не помечается, все в памяти, кетчим эксепшены по каждой из тасок с ретраем. **Проблема**: в случае если сервис остановится в процессе работы, будет ретрай (так как коммита офсета не будет). Это не очень хорошо (двойное списание). Можно сделать доп. таблицу и хранить для каждого запуска последнего обработанного на данный момент юзера.
   
3. Сервис аккаунтинга
   * **Про какие сервисы знает**: про сервис авторизации, про сервис-трекер.
   * **Какие ивенты кидает**:
     1. *manager_income_by_day_change* - изменения в табличке с доходом менеджера в течение дня.
     2. *account_log_change* - изменения по аккаунту.
   * **Таблицы**:
     1. *account* - account_id, user_id, balance.
     2. *account_log* - log_id, account_id, type (DEPOSIT/WITHDRAW,CLEAR), extra_ids (тут json помойка с айдишками, чтобы табличка могла быть универсальной и не была завязана только на таски, в этот json можно писать поле task_id), event_time.
     3. *task_price* - task_price_id, task_id, assign_fee, resolve_price, creation_time.
     4. *manager_income_by_day* (табличка агрегирующая данные по менеджерам компании за день) - income_id, date, total_assign_fee, total_resolve_price.
   * **Задачи**:
     * Страница - на ней баланс и аудит лог если не руководство, по дням заработок если руководство.
     * Ручка - получение цен по задаче. Нужно будет сервису аналитики.
     * Ручка - баланс. Просто из *account* берем.
     * Ручка - аудит лог. Валидация роли. Принимает параметр - день (по умолчанию сегодня). Просто берется из *account_log*. Также надо доставать task_id из жсона и по ним ходить в сервис-трекер за полной инфой.
     * Ручка - заработок за день (до месяца). Валидация роли. Просто берется из *manager_income_by_day*.
     * Листенер *task_create* - расчет цены, сохранение в *task_price*.
     * Листенер *task_assign* - списание денег, обновить *account*, *account_log*, *manager_income_by_day*. Отправить *manager_income_by_day_change*. **Проблема**: из-за гонок может так быть что ивент на ассайн пришел, а на создание таски еще нет, тогда пусть обработка падает и ретраится или же dead letter queue.
     * Листенер *task_resolve* - начисление денег, обновить *account*, *account_log*, *manager_income_by_day*. Отправить *manager_income_by_day_change*. Аналогичная история с гонками, просто ретраим если что.
     * Крон по ночам считать для всех юзеров зпшку и отправлять письмо. Обновление *account*, *account_log*.
   
4. Сервис аналитики (вообще в целом можно кафка ивенты лить в хадуп и дашборды через него строить, а не делать свой сервис)
   * **Про какие сервисы знает**: про сервис авторизации, про сервис аккаунтинга.
   * **Какие ивенты кидает**: никакие.
   * **Таблицы**:
     1. *company_data_by_day* (агрегирующая инфа по компании за день) - data_id, date, total_assign_fee, total_resolve_price, negative_balance_account_count.
     2. *remarkable_task_by_day* (самые интересные таски за день) - data_id, date, most_expensive_resolved_task_price.
   * **Задачи**:
     * Страница-дашборд.
     * Ручка - заработок за сегодня (принимать день) и количество отрицательных аккаунтов.
     * Ручка - особые задачи за время, возвращать самую дорогую из закрытых там.
     * Листенер *manager_income_by_day_change*. Изменение *company_data_by_day*.
     * Листенер *account_log_change*. Изменение *company_data_by_day* если меняется знак баланса у юзера.
     * Листенер *task_resolve*. Получаем по айди цены (**проблема**: может так быть что цена еще не получена если быстро закрыли, тогда ретраи до победного / dead letter queue), обновляем *remarkable_task_by_day*.