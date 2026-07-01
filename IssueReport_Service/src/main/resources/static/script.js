const titleEl = document.getElementById('title');
const categoryEl = document.getElementById('category');
const locationEl = document.getElementById('location');
const descEl = document.getElementById('description');
const photoEl = document.getElementById('photo');

const titleHint = document.getElementById('titleHint');
const categoryHint = document.getElementById('categoryHint');
const locationHint = document.getElementById('locationHint');
const descHint = document.getElementById('descHint');

const titleCounter = document.getElementById('titleCounter');
const descCounter = document.getElementById('descCounter');

const btn = document.getElementById('submitBtn');
const btnLabel = document.getElementById('btnLabel');
const loaderRoad = document.getElementById('loaderRoad');
const result = document.getElementById('result');
const locateBtn = document.getElementById('locateBtn');
const themeToggle = document.getElementById('themeToggle');

// ---- Theme ----
function applyTheme(theme){
  if(theme){
    document.documentElement.setAttribute('data-theme', theme);
  } else {
    document.documentElement.removeAttribute('data-theme');
  }
  const prefersDark = window.matchMedia('(prefers-color-scheme: dark)').matches;
  const effective = theme || (prefersDark ? 'dark' : 'light');
  themeToggle.querySelectorAll('button').forEach(b=>{
    b.classList.toggle('active', b.dataset.theme === effective);
  });
}
themeToggle.querySelectorAll('button').forEach(b=>{
  b.addEventListener('click', () => applyTheme(b.dataset.theme));
});
applyTheme(null);

// ---- Counters ----
function updateCounter(el, counterEl, max){
  const len = el.value.length;
  counterEl.textContent = `${len} / ${max}`;
  counterEl.classList.toggle('near-limit', len > max * 0.9);
}
titleEl.addEventListener('input', () => updateCounter(titleEl, titleCounter, 80));
descEl.addEventListener('input', () => updateCounter(descEl, descCounter, 500));

// ---- Geolocation ----
locateBtn.addEventListener('click', () => {
  if(!navigator.geolocation){
    locationHint.textContent = 'Geolocation is not supported by this browser.';
    return;
  }
  locationHint.textContent = '';
  locateBtn.textContent = '…';
  navigator.geolocation.getCurrentPosition(
    (pos) => {
      const { latitude, longitude } = pos.coords;
      locationEl.value = `${latitude.toFixed(5)}, ${longitude.toFixed(5)}`;
      locateBtn.textContent = '📍';
      locateBtn.classList.add('active');
    },
    () => {
      locationHint.textContent = 'Could not get your location. Type it in manually instead.';
      locateBtn.textContent = '📍';
    }
  );
});

// ---- Validation ----
function clearValidation(){
  titleEl.classList.remove('invalid');
  categoryEl.classList.remove('invalid');
  descEl.classList.remove('invalid');
  titleHint.textContent = '';
  categoryHint.textContent = '';
  descHint.textContent = '';
}

function validate(){
  clearValidation();
  let ok = true;
  if(!titleEl.value.trim()){
    titleEl.classList.add('invalid');
    titleHint.textContent = "Title can't be empty.";
    ok = false;
  }
  if(!categoryEl.value){
    categoryEl.classList.add('invalid');
    categoryHint.textContent = "Please select a category.";
    ok = false;
  }
  if(!descEl.value.trim()){
    descEl.classList.add('invalid');
    descHint.textContent = 'Add a short description of the issue.';
    ok = false;
  }
  return ok;
}

function setLoading(isLoading){
  btn.disabled = isLoading;
  if(isLoading){
    btnLabel.textContent = 'Sending...';
    loaderRoad.classList.add('active');
  } else {
    btnLabel.textContent = 'Submit Report';
    loaderRoad.classList.remove('active');
  }
}

function showResult(type, html){
  result.className = 'show ' + type;
  result.innerHTML = html;
}

async function submitReport(){
  if(!validate()) return;

  const title = titleEl.value.trim();
  const categoryId = categoryEl.value;
  const description = descEl.value.trim();
  const location = locationEl.value.trim();

  // Simulate Photo Array for MVP Backend
  const photos = [];
  if(photoEl.files.length > 0) {
     photos.push({ fileUrl: "dummy_s3_url/" + photoEl.files[0].name });
  }

  const payload = {
    reporterId: "123e4567-e89b-12d3-a456-426614174000",
    title,
    categoryId,
    description,
    location: location || null,
    photos: photos.length > 0 ? photos : null
  };

  setLoading(true);
  result.className = '';
  result.innerHTML = '';

  try {
    const response = await fetch('/api/reports', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    if(response.status === 201){
      const data = await response.json();
      showResult('success', `
        <div class="result-head"><span class="badge">✓</span> Report submitted</div>
        <div class="result-body">
          Report ID: <code>${data.reportId}</code><br>
          <span class="status-chip">${data.status}</span>
        </div>
      `);
      titleEl.value = '';
      categoryEl.value = '';
      descEl.value = '';
      locationEl.value = '';
      photoEl.value = '';
      updateCounter(titleEl, titleCounter, 80);
      updateCounter(descEl, descCounter, 500);
      locateBtn.classList.remove('active');
    } else {
      throw new Error('Failed to submit the report (status ' + response.status + ')');
    }
  } catch(err){
    showResult('error', `
      <div class="result-head"><span class="badge">!</span> Submission failed</div>
      <div class="result-body">${err.message}. Please try again in a moment.</div>
    `);
  } finally {
    setLoading(false);
  }
}

[titleEl, categoryEl, descEl].forEach(el => {
  el.addEventListener('input', () => el.classList.remove('invalid'));
});
