document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById('calendar');
    var calendar = new FullCalendar.Calendar(calendarEl, {
        initialView: 'dayGridMonth',
        headerToolbar: false,
        firstDay: 1, // 从周一开始
        events: generateEvents(),
        eventContent: function(arg) {
            if (arg.event.extendedProps.hasRecord) {
                return {
                    html: '<div class="fc-daygrid-day-number">' + arg.dayNumberText + '</div>' +
                          '<div class="record-indicator">' + arg.event.extendedProps.cardInfo + '</div>'
                };
            }
        },
        dayCellClassNames: function(arg) {
            if (arg.date.toDateString() === new Date().toDateString()) {
                return ['today'];
            }
            return [];
        },
        dateClick: function(info) {
            var event = calendar.getEvents().find(e => e.start.toDateString() === info.date.toDateString());
            if (event && event.extendedProps.hasRecord) {
                alert(`查看记录：${info.dateStr} - ${event.extendedProps.cardInfo}`);
            } else {
                alert(`添加记录：${info.dateStr}`);
            }
        }
    });

    calendar.render();

    function generateEvents() {
        var events = [];
        var currentDate = new Date();
        for (var i = -30; i < 30; i++) {
            var date = new Date(currentDate.getFullYear(), currentDate.getMonth(), currentDate.getDate() + i);
            if (Math.random() > 0.5) {
                events.push({
                    start: date,
                    allDay: true,
                    extendedProps: {
                        hasRecord: true,
                        cardInfo: Math.random() > 0.5 ? '5星' : '4星'
                    }
                });
            }
        }
        return events;
    }

    document.getElementById('prevBtn').addEventListener('click', function() {
        calendar.prev();
        updateNavigationButtons();
    });

    document.getElementById('nextBtn').addEventListener('click', function() {
        calendar.next();
        updateNavigationButtons();
    });

    document.getElementById('todayBtn').addEventListener('click', function() {
        calendar.today();
        updateNavigationButtons();
    });

    document.getElementById('viewMode').addEventListener('change', function() {
        calendar.changeView(this.value);
        updateNavigationButtons();
    });

    function updateNavigationButtons() {
        var currentDate = calendar.getDate();
        var today = new Date();
        today.setHours(0, 0, 0, 0);

        var isCurrentMonth = currentDate.getMonth() === today.getMonth() && currentDate.getFullYear() === today.getFullYear();
        var isCurrentWeek = calendar.view.type === 'dayGridWeek' && currentDate <= today && today <= calendar.view.currentEnd;

        document.getElementById('nextBtn').disabled = isCurrentMonth || isCurrentWeek;
        document.getElementById('todayBtn').style.display = (isCurrentMonth || isCurrentWeek) ? 'none' : 'inline-block';
    }

    updateNavigationButtons();

    const registerFiveStarBtn = document.getElementById('registerFiveStarBtn');
    const fiveStarModal = document.getElementById('fiveStarModal');
    const cardList = document.getElementById('cardList');
    const saveCardBtn = document.getElementById('confirmCardBtn');
    const cancelCardBtn = document.getElementById('cancelCardBtn');
    const tabButtons = document.querySelectorAll('.tab-btn');
    const cardSearchInput = document.getElementById('cardSearch');
    const pullCardTypeInputs = document.querySelectorAll('input[name="pullCardType"]');
    const loadingIndicator = document.getElementById('loadingIndicator');
    const searchBtn = document.getElementById('searchBtn');
    const priceOptions = document.getElementById('priceOptions');



    let cards = []; // 存储从端获取的卡牌数据
    let selectedCard = null;
    let selectedPullCardType = '1'; // 默认选择免费
    let currentFaction = '0';

    function openFiveStarModal() {
        fiveStarModal.style.display = 'block';
        if (loadingIndicator) {
            loadingIndicator.style.display = 'block';  // 显示加载指示器
        }
        cardSearchInput.value = '';
        selectedCard = null;
        selectedPullCardType = '1';
        //document.querySelector('input[name="pullCardType"][value="1"]').checked = true;
        updateSaveButtonState();
        
        // 重置阵营选择
        tabButtons.forEach(btn => btn.classList.remove('active'));
        const allFactionTab = document.querySelector(`.tab-btn[data-faction="0"]`);
        if (allFactionTab) {
            allFactionTab.classList.add('active');
        } else {
            console.error('未找到"全部"阵营的标签按钮');
        }
        currentFaction = '0';

        fetchCards(0,'', 5);  // 加载所有5星武将数据
    }

    // 为搜索按钮添加点击事件监听器
    searchBtn.addEventListener('click', function() {
        const searchHeroName = cardSearchInput.value.trim(); // 获取输入的武将名
         // 获取选中的阵营
         const selectedFaction = this.dataset.faction;
        fetchCards(selectedFaction, searchHeroName); // 调用 fetchCards 函数，传递当前阵营和搜索词
    });

    // 为搜索框添加输入事件监听器
    cardSearchInput.addEventListener('input', function() {
        const searchHeroName = this.value.trim(); // 获取输入的武将名
         // 获取选中的阵营
         const selectedFaction = this.dataset.faction;
        fetchCards(selectedFaction, searchHeroName); // 调用 fetchCards 函数，传递当前阵营和搜索词
    });

    function fetchCards(faction = 0, heroName = '') {
        fetchCards(faction,heroName,5);
    }

    function fetchCards(faction = 0, heroName = '', heroStarLevel = 5) {
        let requestBody = {
            hero: {
                faction: faction,
                heroStarLevel: heroStarLevel,
                heroName: heroName // 添加搜索词
            },
            query: {
                page: 1,
                size: 1000,  // 假设我们想一次性获取所有卡牌，可以根据需要调整
                totalRecordNum: 0  // 这个值会由后端填充
            }
        };

        fetch('http://localhost:8080/hero/getHeroes', {
            method: 'POST',
            headers: {
                'Content-Type': 'application/json',
            },
            body: JSON.stringify(requestBody)
        })
        .then(response => response.json())
        .then(data => {
            console.log('Received data:', data); // 添加这行来查看接收到的数据结构

            // 检查数据结构并相应地处理
            let heroesData = [];
            if (data.query && data.query.queryResultJson) {
                try {
                    heroesData = JSON.parse(data.query.queryResultJson);
                } catch (error) {
                    console.error('Error parsing queryResultJson:', error);
                }
            }
            
            cards = heroesData.map(hero => ({
                id: hero.heroId,
                name: hero.heroName,
                faction: getFactionName(hero.faction),
                image: hero.webHeroPic,
                starLevel: hero.heroStarLevel
            }));
            renderCards(cards);
            if (loadingIndicator) {
                loadingIndicator.style.display = 'none';  // 隐藏加载指示器
            }
        })
        .catch(error => {
            console.error('Error:', error);
            if (loadingIndicator) {
                loadingIndicator.style.display = 'none';  // 出错时也要隐藏加载指示器
            }
            // 显示错误消息给用户
            alert('加载武将数据时出错，请稍后再试。');
        });
    }

    // 更新 getFactionId 函数
    function getFactionId(factionName) {
        const factions = {
            '全部': 0,
            '魏': 1,
            '蜀': 2,
            '吴': 3,
            '汉': 4,
            '群': 5,
            '晋': 6
        };
        return factions[factionName] || 0;  // 如果找不到对应的 ID，返回 0（全部）
    }

    // 保持 getFactionName 函数不变
    function getFactionName(factionId) {
        const factions = {
            0: '全部',
            1: '魏',
            2: '蜀',
            3: '吴',
            4: '汉',
            5: '群',
            6: '晋'
        };
        return factions[factionId] || '未知';
    }

    // 点击其他地方时隐藏下拉菜单
    document.addEventListener('click', function(event) {
        const priceOptions = document.getElementById('priceOptions');
        const optionsContainer = priceOptions.querySelector('.options');

        // 检查点击是否在下拉菜单或卡片上
        if (!priceOptions.contains(event.target)) {
            optionsContainer.style.display = 'none'; // 隐藏选项
        }
    });

    function renderCards(cardsToRender) {
        cardList.innerHTML = '';
        cardsToRender.forEach(card => {
            const cardElement = document.createElement('div');
            cardElement.className = 'card-item';
            cardElement.setAttribute('data-selected', 'false'); // 添加选中状态属性
            cardElement.innerHTML = `
                <img src="${card.image}" alt="${card.name}">
                <p>${card.name}</p>
                <p>${card.faction} ${card.starLevel}星</p>
            `;
            cardElement.addEventListener('click', (event) => selectCard(card, cardElement, event)); // 传递事件对象
            cardList.appendChild(cardElement);
        });
    }

    function filterCards(faction, searchTerm = '') {
        let filteredCards = cards;
        
        if (faction !== 'all') {
            filteredCards = filteredCards.filter(card => card.faction === faction);
        }

        if (searchTerm) {
            filteredCards = filteredCards.filter(card => 
                card.name.toLowerCase().includes(searchTerm.toLowerCase())
            );
        }

        renderCards(filteredCards);
    }

    function selectCard(card, cardElement, event) {
        // 阻止事件冒泡
        event.stopPropagation();

        // 取消之前选中的卡牌
        const previouslySelected = document.querySelector('.card-item[data-selected="true"]');
        if (previouslySelected) {
            previouslySelected.setAttribute('data-selected', 'false');
            previouslySelected.style.backgroundColor = ''; // 恢复背景色
        }

        // 选中当前卡牌
        selectedCard = card;
        cardElement.setAttribute('data-selected', 'true');
        cardElement.style.backgroundColor = '#e6f7ff'; // 设置选中背景色

        // 显示价格选项下拉框
        const priceOptions = document.getElementById('priceOptions');
        const optionsContainer = priceOptions.querySelector('.options');
        priceOptions.style.display = 'block';
        optionsContainer.style.display = 'block'; // 显示选项

        // 设置下拉框位置
        priceOptions.style.left =  `${event.clientX}px`;
        priceOptions.style.top = `${event.clientY}px`;

        // 确保下拉框不会超出视口边界
        const viewportWidth = window.innerWidth;
        const viewportHeight = window.innerHeight;
        const priceOptionsWidth = priceOptions.offsetWidth;
        const priceOptionsHeight = priceOptions.offsetHeight;

        if (event.clientX + priceOptionsWidth > viewportWidth) {
            priceOptions.style.left = `${viewportWidth - priceOptionsWidth}px`;
        }
        if (event.clientY + priceOptionsHeight > viewportHeight) {
            priceOptions.style.top = `${viewportHeight - priceOptionsHeight}px`;
        }

        updateSaveButtonState();
    }

    function updateSaveButtonState() {
        saveCardBtn.disabled = !(selectedCard && selectedPullCardType);
    }

    function saveSelectedCard() {
        if (selectedCard && selectedPullCardType) {
            // 这里应该发送请求到后端保存选中的卡牌和抽卡方式
            console.log('保存选中的卡牌:', selectedCard, '抽卡方式:', selectedPullCardType);
            // 更新日历上的显示
            // 这里需要根据你的日历实现来更新显示
            closeFiveStarModal();
        } else {
            alert('请选择一张卡牌和抽卡方式');
        }
    }

    function closeFiveStarModal() {
        fiveStarModal.style.display = 'none';
        cardSearchInput.value = '';
    }

    const closeModalBtn = document.getElementById('closeModalBtn');

    closeModalBtn.addEventListener('click', function() {
        fiveStarModal.style.display = 'none';
    });

    registerFiveStarBtn.addEventListener('click', openFiveStarModal);
    saveCardBtn.addEventListener('click', saveSelectedCard);
    cancelCardBtn.addEventListener('click', closeFiveStarModal);

    tabButtons.forEach(button => {
        button.addEventListener('click', function() {
            // 移除所有按钮的 active 类
            tabButtons.forEach(btn => btn.classList.remove('active'));
            // 为当前点击的按钮添加 active 类
            this.classList.add('active');

            // 获取选中的阵营
            const selectedFaction = this.dataset.faction;

            // 调用 fetchCards 函数
            fetchCards(selectedFaction);
        });
    });

    cardSearchInput.addEventListener('input', function() {
        const heroName = this.value.trim(); // 获取输入的武将名
        fetchCards(selectedFaction, heroName); // 调用 fetchCards 函数，传递当前阵营和搜索词
    });

    pullCardTypeInputs.forEach(radio => {
        radio.addEventListener('change', function() {
            selectedPullCardType = this.value;
            updateSaveButtonState();
        });
    });

    const pullCardTypeSelect = document.getElementById('pullCardTypeSelect');
    const optionsContainer = pullCardTypeSelect.querySelector('.options');

    // 点击选项时更新选中状态
    pullCardTypeSelect.addEventListener('click', function() {
        optionsContainer.style.display = optionsContainer.style.display === 'none' ? 'block' : 'none';
    });

    // 处理选项点击
    optionsContainer.querySelectorAll('.option').forEach(option => {
        option.addEventListener('click', function(event) {
            event.stopPropagation(); // 阻止事件冒泡

            const value = this.getAttribute('data-value');
            const selectedOption = document.querySelector('.selected-option');
            selectedOption.textContent = this.textContent; // 更新显示为选中的选项

            // 显示确认窗口
            const confirmationModal = document.getElementById('confirmationModal');
            const cardImage = document.getElementById('cardImage');
            const cardName = document.getElementById('cardName');
            const pullCardType = document.getElementById('pullCardType');

            // 填充武将卡信息
            cardImage.src = selectedCard.image; // 假设 selectedCard 包含武将卡信息
            cardName.textContent = selectedCard.name; // 假设 selectedCard 包含武将名称
            pullCardType.textContent = `抽卡类型: ${this.textContent}`; // 显示抽卡类型

            confirmationModal.style.display = 'block'; // 显示确认窗口
        });
    });

    // 处理确认按钮点击
    document.getElementById('confirmCardBtn').addEventListener('click', function() {
        // 在这里添加登记逻辑
        console.log('登记成功');
        document.getElementById('confirmationModal').style.display = 'none'; // 隐藏确认窗口
    });

    // 处理取消按钮点击
    document.getElementById('cancelCardBtn').addEventListener('click', function() {
        document.getElementById('confirmationModal').style.display = 'none'; // 隐藏确认窗口
    });

    // 处理关闭按钮点击
    document.getElementById('closeModalBtn').addEventListener('click', function() {
        document.getElementById('confirmationModal').style.display = 'none'; // 隐藏确认窗口
    });
});