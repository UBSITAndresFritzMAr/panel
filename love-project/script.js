
document.addEventListener('DOMContentLoaded', () => {


  const loader        = document.getElementById('loader');
  const enterBtn       = document.getElementById('enter-btn');
  const nav            = document.getElementById('navbar');
  const hamburger      = document.getElementById('hamburger');
  const navLinks       = document.getElementById('navLinks');
  const musicToggle    = document.getElementById('music-toggle');
  const music          = document.getElementById('bg-music');
  const letterModal    = document.getElementById('letter-modal');
  const letterTitleEl  = document.getElementById('letter-title');
  const letterBodyEl   = document.getElementById('letter-body');
  const surpriseModal  = document.getElementById('surprise-modal');
  const surpriseBtn    = document.getElementById('surprise-btn');
  const ambient        = document.getElementById('ambient');

  let lastFocusedEl = null; 


  enterBtn.addEventListener('click', () => {
    loader.classList.add('hidden');
    music.play().catch(() => {
     
    });
    musicToggle.textContent = '❚❚';
    musicToggle.setAttribute('aria-pressed', 'true');
  }, { once: true });

  window.addEventListener('scroll', () => {
    nav.classList.toggle('scrolled', window.scrollY > 40);
  }, { passive: true });

  hamburger.addEventListener('click', () => {
    const isOpen = navLinks.classList.toggle('open');
    hamburger.setAttribute('aria-expanded', String(isOpen));
  });

  navLinks.querySelectorAll('a').forEach((link) => {
    link.addEventListener('click', () => {
      navLinks.classList.remove('open');
      hamburger.setAttribute('aria-expanded', 'false');
    });
  });


  musicToggle.addEventListener('click', () => {
    if (music.paused) {
      pauseAllSongPlayers(); 
      music.play().catch(() => {});
      musicToggle.textContent = '❚❚';
      musicToggle.setAttribute('aria-pressed', 'true');
    } else {
      music.pause();
      musicToggle.textContent = '♪';
      musicToggle.setAttribute('aria-pressed', 'false');
    }
  });

  music.addEventListener('pause', () => {
    musicToggle.textContent = '♪';
    musicToggle.setAttribute('aria-pressed', 'false');
  });

 
  const songPlayers = [];

  function formatTime(seconds) {
    if (!isFinite(seconds) || seconds < 0) return '0:00';
    const mins = Math.floor(seconds / 60);
    const secs = Math.floor(seconds % 60).toString().padStart(2, '0');
    return `${mins}:${secs}`;
  }

  function pauseAllSongPlayers(except) {
    songPlayers.forEach(({ audio, button }) => {
      if (audio === except) return;
      audio.pause();
    });
  }

  document.querySelectorAll('.song-card').forEach((card) => {
    const audio = card.querySelector('.track-audio');
    const button = card.querySelector('.play-pause-btn');
    const progressBar = card.querySelector('.progress-bar');
    const progressFill = card.querySelector('.progress-fill');
    const currentTimeEl = card.querySelector('.current-time');
    const durationEl = card.querySelector('.duration');

    if (!audio || !button) return; 
    songPlayers.push({ audio, button });


    const lyricsContainer = card.querySelector('.lyrics-container');
    const lyricsScroll = card.querySelector('.lyrics-scroll');
    const songKey = card.dataset.songKey;
    const lines = (typeof songLyrics !== 'undefined' && songKey && songLyrics[songKey]) || [];
    let lineEls = [];
    let activeLineIndex = -1;

    if (lyricsContainer && lyricsScroll) {
      if (lines.length === 0) {
      
        lyricsScroll.innerHTML = '';
        const empty = document.createElement('div');
        empty.className = 'lyrics-empty';
        empty.textContent = 'Add this song\u2019s lyrics in lyrics.js to see them synced here.';
        lyricsScroll.appendChild(empty);
      } else {
        lyricsScroll.innerHTML = '';
        lines.forEach((line, i) => {
          const lineEl = document.createElement('p');
          lineEl.className = 'lyrics-line';
          lineEl.textContent = line.text;
          lineEl.dataset.time = line.time;
        
          lineEl.addEventListener('click', () => {
            audio.currentTime = line.time;
            if (audio.paused) {
              pauseAllSongPlayers(audio);
              music.pause();
              audio.play().catch(() => {});
            }
          });
          lyricsScroll.appendChild(lineEl);
          lineEls.push(lineEl);
        });
      }
    }


    function syncLyrics(currentTime) {
      if (!lineEls.length) return;

      let newIndex = -1;
      for (let i = 0; i < lineEls.length; i++) {
        if (currentTime >= parseFloat(lineEls[i].dataset.time)) {
          newIndex = i;
        } else {
          break;
        }
      }

      if (newIndex === activeLineIndex) return;
      activeLineIndex = newIndex;

      lineEls.forEach((el) => el.classList.remove('active'));

      if (newIndex >= 0) {
        const activeEl = lineEls[newIndex];
        activeEl.classList.add('active');
        if (lyricsContainer) {
          const targetScroll = activeEl.offsetTop
            - (lyricsContainer.clientHeight / 2)
            + (activeEl.clientHeight / 2);
          lyricsContainer.scrollTop = targetScroll;
        }
      } else if (lyricsContainer) {
        lyricsContainer.scrollTop = 0;
      }
    }

  
    let rafId = null;
    function tick() {
      syncLyrics(audio.currentTime);
      rafId = requestAnimationFrame(tick);
    }
    function startSyncLoop() {
      if (rafId === null) rafId = requestAnimationFrame(tick);
    }
    function stopSyncLoop() {
      if (rafId !== null) {
        cancelAnimationFrame(rafId);
        rafId = null;
      }
    }

  
    button.addEventListener('click', () => {
      if (audio.paused) {
        pauseAllSongPlayers(audio); 
        music.pause(); 
        audio.play().catch(() => {
        
          console.warn('Could not play track. Make sure the mp3 file exists in the /music folder.');
        });
      } else {
        audio.pause();
      }
    });

    audio.addEventListener('play', () => {
      button.classList.add('is-playing');
      button.setAttribute('aria-pressed', 'true');
      startSyncLoop();
    });

    audio.addEventListener('pause', () => {
      button.classList.remove('is-playing');
      button.setAttribute('aria-pressed', 'false');
      stopSyncLoop();
    });

    audio.addEventListener('ended', () => {
      button.classList.remove('is-playing');
      button.setAttribute('aria-pressed', 'false');
      stopSyncLoop();
      if (progressFill) progressFill.style.width = '0%';
      if (currentTimeEl) currentTimeEl.textContent = '0:00';
      activeLineIndex = -1;
      lineEls.forEach((el) => el.classList.remove('active'));
      if (lyricsContainer) lyricsContainer.scrollTop = 0;
    });

   
    audio.addEventListener('loadedmetadata', () => {
      if (durationEl) durationEl.textContent = formatTime(audio.duration);
    });

    
    audio.addEventListener('timeupdate', () => {
      if (!audio.duration) return;
      const percent = (audio.currentTime / audio.duration) * 100;
      if (progressFill) progressFill.style.width = `${percent}%`;
      if (currentTimeEl) currentTimeEl.textContent = formatTime(audio.currentTime);
      if (progressBar) progressBar.setAttribute('aria-valuenow', Math.round(percent));
      
      syncLyrics(audio.currentTime);
    });

    
    if (progressBar) {
      progressBar.addEventListener('click', (e) => {
        if (!audio.duration) return;
        const rect = progressBar.getBoundingClientRect();
        const ratio = Math.min(Math.max((e.clientX - rect.left) / rect.width, 0), 1);
        audio.currentTime = ratio * audio.duration;
        syncLyrics(audio.currentTime);
      });

    
      progressBar.addEventListener('keydown', (e) => {
        if (!audio.duration) return;
        const step = 5; 
        if (e.key === 'ArrowRight') {
          audio.currentTime = Math.min(audio.currentTime + step, audio.duration);
          syncLyrics(audio.currentTime);
        } else if (e.key === 'ArrowLeft') {
          audio.currentTime = Math.max(audio.currentTime - step, 0);
          syncLyrics(audio.currentTime);
        }
      });
    }
  });


  const revealEls = document.querySelectorAll('.reveal');

  if ('IntersectionObserver' in window) {
    const io = new IntersectionObserver((entries) => {
      entries.forEach((entry, i) => {
        if (entry.isIntersecting) {
          setTimeout(() => entry.target.classList.add('in-view'), i * 60);
          io.unobserve(entry.target);
        }
      });
    }, { threshold: 0.15 });

    revealEls.forEach((el) => io.observe(el));
  } else {
   
    revealEls.forEach((el) => el.classList.add('in-view'));
  }


  const letters = {
    1: {
      title: 'For the Girl Who Changed Everything',
      body: `My dearest Erika,

I didn't know a heart could hold this much until you came along and quietly rearranged mine.

You walked into my life and somehow made everything softer, brighter, easier to carry. I don't think you know how much you've changed me — for the better, always for the better.

Thank you for being exactly who you are.

Always yours,
Fritz`
    },
    2: {
      title: 'On an Ordinary Tuesday',
      body: `My love,

Nothing happened today. No special date, no occasion — just an ordinary day. And still, I thought of you a hundred times.

That's the thing about you: you don't need a reason to be on my mind. You're just always there, in the quiet parts of my day, making them feel less quiet.

I love you on the big days and the small, forgettable ones too.

Yours,
Fritz`
    },
    3: {
      title: 'For Every Year After This One',
      body: `Erika,

I don't know exactly what our future looks like — but I know I want you in every version of it.

Wherever we end up, whatever we build, I hope it's together. I hope future-us looks back at this little gift and smiles at how far we've come.

Here's to every year after this one.

I love you, always,
Fritz`
    }
  };

  document.querySelectorAll('.envelope').forEach((envelope) => {
    envelope.addEventListener('click', () => {
      const data = letters[envelope.dataset.letter];
      if (!data) return;
      letterTitleEl.textContent = data.title;
      letterBodyEl.textContent = data.body;
      openModal(letterModal, envelope);
    });
  });

 
  surpriseBtn.addEventListener('click', () => {
    openModal(surpriseModal, surpriseBtn);
    burstConfetti();
  });


  function openModal(modal, triggerEl) {
    lastFocusedEl = triggerEl || document.activeElement;
    modal.classList.add('active');
    document.body.style.overflow = 'hidden';
    const closeBtn = modal.querySelector('.close-modal');
    if (closeBtn) closeBtn.focus();
  }

  function closeModal(modal) {
    modal.classList.remove('active');
    document.body.style.overflow = '';
    if (lastFocusedEl) lastFocusedEl.focus();
  }

  document.querySelectorAll('.close-modal').forEach((btn) => {
    btn.addEventListener('click', () => {
      if (btn.dataset.close === 'letter') closeModal(letterModal);
      if (btn.dataset.close === 'surprise') closeModal(surpriseModal);
    });
  });

  [letterModal, surpriseModal].forEach((modal) => {
    modal.addEventListener('click', (e) => {
      if (e.target === modal) closeModal(modal);
    });
  });

  document.addEventListener('keydown', (e) => {
    if (e.key !== 'Escape') return;
    if (letterModal.classList.contains('active')) closeModal(letterModal);
    if (surpriseModal.classList.contains('active')) closeModal(surpriseModal);
  });


  function burstConfetti() {
    const emojis = ['❤', '💕', '🌸', '✨', '💗'];
    const pieces = 26;

    for (let i = 0; i < pieces; i++) {
      setTimeout(() => {
        const piece = document.createElement('div');
        piece.className = 'confetti';
        piece.textContent = emojis[Math.floor(Math.random() * emojis.length)];
        piece.style.left = `${Math.random() * 100}vw`;
        piece.style.fontSize = `${0.9 + Math.random() * 1.2}rem`;
        piece.style.animationDuration = `${2.5 + Math.random() * 2}s`;
        document.body.appendChild(piece);
        setTimeout(() => piece.remove(), 5000);
      }, i * 60);
    }
  }


  const floatEmojis = ['❤', '💗', '🌸'];

  function spawnHeart() {
    const heart = document.createElement('div');
    heart.className = 'floaty';
    heart.textContent = floatEmojis[Math.floor(Math.random() * floatEmojis.length)];
    heart.style.left = `${Math.random() * 100}vw`;
    heart.style.setProperty('--drift', `${Math.random() * 140 - 70}px`);
    heart.style.animationDuration = `${9 + Math.random() * 8}s`;
    heart.style.color = Math.random() > 0.5
      ? 'rgba(217,138,156,0.6)'
      : 'rgba(211,173,112,0.6)';
    ambient.appendChild(heart);
    setTimeout(() => heart.remove(), 18000);
  }

  function spawnSparkle() {
    const sparkle = document.createElement('div');
    sparkle.className = 'sparkle';
    sparkle.style.left = `${Math.random() * 100}vw`;
    sparkle.style.top = `${Math.random() * 100}vh`;
    sparkle.style.animationDelay = `${Math.random() * 2}s`;
    ambient.appendChild(sparkle);
    setTimeout(() => sparkle.remove(), 4000);
  }

  const prefersReducedMotion = window.matchMedia('(prefers-reduced-motion: reduce)').matches;

  if (!prefersReducedMotion) {
    setInterval(spawnHeart, 1600);
    for (let i = 0; i < 4; i++) setTimeout(spawnHeart, i * 500);
    setInterval(spawnSparkle, 900);
  }

});
